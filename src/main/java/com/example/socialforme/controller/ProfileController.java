package com.example.socialforme.controller;

import com.example.socialforme.model.Info;
import com.example.socialforme.model.User;
import com.example.socialforme.service.InfoService;
import com.example.socialforme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final InfoService infoService;

    @Autowired
    public ProfileController(UserService userService, InfoService infoService) {
        this.userService = userService;
        this.infoService = infoService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping
    public String getProfile(Principal principal, Model model) {
        User user = userService.getUser(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("flag", true);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        return "profile/profile";
    }

    @RequestMapping("/editLink")
    public String editLinks(Principal principal, Model model) {
        User user = userService.getUser(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new Info());
        model.addAttribute("flag", true);
        return "profile/editLinks";
    }

    @PostMapping("/editLink")
    public String saveNewLinks(@ModelAttribute("info") Info info, Principal principal) {
        User user = userService.getUser(principal.getName());
        if (user.getInfo() == null) {
            infoService.save(info);
            user.setInfo(info);
            userService.save(user);
            return "redirect:/profile";
        }

        Info info1 = user.getInfo();
        info1.setGithub(info.getGithub());
        info1.setTwitter(info.getTwitter());
        info1.setInstagram(info.getInstagram());
        info1.setFacebook(info.getFacebook());
        infoService.save(info1);
        return "redirect:/profile";
    }

    @RequestMapping("/editProfile")
    public String editInfo(Principal principal, Model model) {
        User user = userService.getUser(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new Info());
        model.addAttribute("flag", true);
        return "profile/editProfile";
    }

    @PostMapping("/editProfile")
    public String editNewProfile(@ModelAttribute("info") Info info, Principal principal) {
        User user = userService.getUser(principal.getName());
        if (user.getInfo() == null) {
            infoService.save(info);
            user.setInfo(info);
            userService.save(user);
            return "redirect:/profile";
        }

        Info info1 = user.getInfo();
        info1.setEmail(info.getEmail());
        info1.setTelephone(info.getTelephone());
        info1.setCity(info.getCity());
        infoService.save(info1);
        return "redirect:/profile";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        User user = userService.getUser(principal.getName());
        Info info = user.getInfo();
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String dir = System.getProperty("user.dir");
            System.out.println(dir);
            File uploadDir = new File(dir + "/" + uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuid = UUID.randomUUID().toString();
            String resultFile = uuid + "." + file.getOriginalFilename();
            if (info != null) {
                info.setImage(resultFile);
                infoService.save(info);
            } else {
                Info info1 = new Info();
                info1.setImage(resultFile);
                infoService.save(info1);
                user.setInfo(info1);
                userService.save(user);
            }


            file.transferTo(new File(dir + "/" +uploadPath + "/" + resultFile));
        }
        return "redirect:/profile";
    }

}
