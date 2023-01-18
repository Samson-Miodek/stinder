package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Dialogue;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.DialogueRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class MessengerController {


    @Value("${site.host}")
    private String HOST;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private DialogueRepository dialogueRepository;

    @Autowired
    private UserRepository userRepository;


    //диалоги
    @GetMapping("/messenger")
    public String getMessenger(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("dialogues", user.getDialogues());
        model.addAttribute("myId",user.getId());
        return "messenger/messenger";
    }

    //диалог с пользователем с id=123
    @GetMapping("/messenger/{id}")
    public String getDialogue(@PathVariable(name = "id") long userToId, Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<User> optUserTo = userRepository.findById(userToId);

        if(!optUserTo.isPresent())
            return "redirect:/";
        User userTo = optUserTo.get();
//
//        user.getDialogueUsers().add(userTo);
//        userRepository.save(user);
        List<Message> messages = messageRepository.findAllByUserFromAndUserToOrUserToAndUserFrom(user, userTo, user, userTo);


        model.addAttribute("messages", messages);
        model.addAttribute("userTo", userTo);
        model.addAttribute("myId", user.getId());
        model.addAttribute("dialogues",user.getDialogues());

        return "messenger/dialogue";
    }

    @PostMapping("/sendMessageDialogue")
    public String postSendMessageDialogue(@RequestParam("text") String text,
                                          @RequestParam("toId") long toId,
                                          Principal principal,
                                          HttpServletRequest request) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<User> optUserTo = userRepository.findById(toId);

        if(!optUserTo.isPresent())
            return "redirect:/";

        User userTo = optUserTo.get();

        Message message = new Message();
        message.setUserFrom(user);
        message.setUserTo(userTo);
        message.setText(text);
        message.setDate(new Timestamp(new Date().getTime()));

        Dialogue dialogue = dialogueRepository.findByUserfromAndUsertoOrUsertoAndUserfrom(user,userTo,user,userTo);

        if(dialogue == null){
            dialogue = new Dialogue();
            dialogue.setUserfrom(user);
            dialogue.setUserto(userTo);
        }
        dialogue.setLastMessage(message);


        user.getDialogues().add(dialogue);
        userTo.getDialogues().add(dialogue);

        messageRepository.save(message);
        dialogueRepository.save(dialogue);
        userRepository.save(user);
        userRepository.save(userTo);

        return "redirect:" + request.getHeader("Referer");
    }


    //чаты
    @GetMapping("/messenger/chats")
    public String getChats(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());

        model.addAttribute("chats",user.getChats());

        return "messenger/messChats";
    }


    //     createChat
    @GetMapping("/messenger/createChat")
    public String getCreateChat() {
        return "messenger/createChat";
    }


    @PostMapping("/messenger/createChat")
    public String postCreateChat(@RequestParam("name") String name, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());

        Chat chat = new Chat();
        chat.setName(name);
        chat.setInviteCode(UUID.randomUUID().toString());
        chat.setUsers(new HashSet<>());
        chat.getUsers().add(user);

        if(user.getChats() == null)
            user.setChats(new HashSet<>());
        user.getChats().add(chat);

        chatRepository.save(chat);
        userRepository.save(user);

        return "redirect:/messenger/chats";
    }



    @GetMapping("/messenger/chat/{id}")
    public String getChat(@PathVariable(name = "id") long chatId, Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Chat> chat = chatRepository.findById(chatId);

        if(!chat.isPresent())
            return "redirect:/";

        //ввести код
        if(!chat.get().getUsers().contains(user))
            return "redirect:/messenger/invite/chat/"+chatId;


        model.addAttribute("messages", chat.get().getMessages());
        model.addAttribute("chat", chat.get());
        model.addAttribute("chats", user.getChats());
        model.addAttribute("myId", user.getId());
        model.addAttribute("hostUrl", HOST);
        return "messenger/chat";
    }


    @PostMapping("/sendMessageChat")
    public String postSendMessageChat(@RequestParam("text") String text,
                                          @RequestParam("chatId") long chatId,
                                          Principal principal,
                                          HttpServletRequest request) {
        User user = userRepository.findByEmail(principal.getName());

        Chat chat = chatRepository.findById(chatId).get();

        Message message = new Message();
        message.setUserFrom(user);
        message.setText(text);
        message.setChat(chat);
        message.setDate(new Timestamp(new Date().getTime()));

        chat.getMessages().add(message);
        chat.setLastMessage(message);

        messageRepository.save(message);
        chatRepository.save(chat);

        return "redirect:" + request.getHeader("Referer");
    }




    @GetMapping("/messenger/invite/chat/{id}")
    public String getChatInvite(@PathVariable(name = "id") long chatId,Principal principal, Model model) {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if(!chat.isPresent())
            return "redirect:/";

        model.addAttribute("chatId",chatId);

        return "messenger/chatInvite";
    }


    @PostMapping("/messenger/invite/chat")
    public String postChatInvite(@RequestParam("code") String code,
                                      @RequestParam("chatId") long chatId,
                                      Principal principal,
                                      HttpServletRequest request) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(!chat.isPresent())
            return "redirect:/";

        if(chat.get().getInviteCode().equals(code)){
            chat.get().getUsers().add(user);
            user.getChats().add(chat.get());

            userRepository.save(user);
            chatRepository.save(chat.get());
        }else{
            return "redirect:/messenger/invite/chat/"+chatId;
        }


        return "redirect:/messenger/chat/"+chatId;
    }

    @GetMapping("/messenger/invite/chat/{chatId}/{code}")
    public String chatInviteLink(@PathVariable("code") String code,
                                 @PathVariable("chatId") long chatId,
                                 Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(!chat.isPresent())
            return "redirect:/";

        if(chat.get().getInviteCode().equals(code)){
            chat.get().getUsers().add(user);
            user.getChats().add(chat.get());

            userRepository.save(user);
            chatRepository.save(chat.get());
        }else{
            return "redirect:/messenger/invite/chat/"+chatId;
        }


        return "redirect:/messenger/chat/"+chatId;
    }


//
//    @GetMapping("/messenger/chat/")
//    public String getMessenger(@RequestParam(name = "f") long chadId, Principal principal) {
//        User user = userRepository.findByEmail(principal.getName());
//        long id = user.getId();
//        long anotherUserId = user.getId();
//        for (User u: chatRepository.findById(chadId).get().getUsers()) {
//            if(u.getId()!=user.getId())
//            {
//                anotherUserId = u.getId();
//                break;
//            }
//        }
//        System.out.println(chadId);
//        return "redirect:./"+anotherUserId;
//    }
//
//
//    @GetMapping("/messenger/chat/{id}")
//    public String getChat(@PathVariable long id, Principal principal, Model model) {
//
//        User user = userRepository.findByEmail(principal.getName());
//        User another = userRepository.findById(id).get();
//
//        Set<User> s = new HashSet<>();
//        s.add(user);
//        s.add(another);
//
//        Message message = messageRepository.findFirstByUserFromAndUserTo(user, another);
//        if (message == null) {
//            message = messageRepository.findFirstByUserFromAndUserTo(another, user);
//        }
//
//        if (message == null) {
//            System.out.println("Chat is null");
//            Chat chat = new Chat();
//            String inviteCode = UUID.randomUUID().toString();
//            chat.setUsers(s);
//            Message welcome = new Message(user, another, chat, "Ссылка на приглашение в чат: "+HOST+"/invite/"+inviteCode);
//            chat.setMessages(new ArrayList<>());
//            chat.getMessages().add(welcome);
//            chat.setLastMessage(message);
//            chat.setInviteCode(inviteCode);
//
//            chatRepository.save(chat);
//            messageRepository.save(welcome);
//
//            user.getChats().add(chat);
//            another.getChats().add(chat);
//            userRepository.save(user);
//            userRepository.save(another);
//        } else {
//            long chatId = message.getChat().getId();
//            Chat chat = chatRepository.findById(chatId).get();
//            System.out.println("EXIST " + chat);
//            model.addAttribute("messages", chat.getMessages());
//            model.addAttribute("idTo", another.getId());
//            model.addAttribute("chatId", chatId);
//            model.addAttribute("usrs", chat.getUsers());
//        }
//
//
//        return "chat";
//    }
//
//    @PostMapping("/sendMessage")
//    public String postSendMessage(@RequestParam("text") String text,
//                                  @RequestParam("toId") long toId,
//                                  @RequestParam("chatId") long chatId,
//                                  Principal principal,
//                                  HttpServletRequest request){
//
//        Message message = new Message();
//        message.setUserFrom(userRepository.findByEmail(principal.getName()));
//        message.setUserTo(userRepository.findById(toId).get());
//        message.setText(text);
//
//        Chat chat = chatRepository.findById(chatId).get();
//        chat.setLastMessage(message);
//        message.setChat(chat);
//
//        messageRepository.save(message);
//        chatRepository.save(chat);
//
//        return "redirect:"+request.getHeader("Referer");
//    }
//
//    @GetMapping("/invite/{code}")
//    public String activateEmail(@PathVariable String code, Principal principal) {
//        User user = userRepository.findByEmail(principal.getName());
//        Chat chat = chatRepository.findByInviteCode(code);
//
//        System.out.println(chat);
//
//        Message message = new Message();
//        message.setText(user.getName()+" has added");
//
//        chat.getUsers().add(user);
//        chat.getMessages().add(message);
//        chat.setLastMessage(message);
//
//        user.getChats().add(chat);
//        userRepository.save(user);
//        chatRepository.save(chat);
//        messageRepository.save(message);
//        return "redirect:/messenger";
//    }

}
