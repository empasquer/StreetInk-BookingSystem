package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProfileController {
    @Autowired
    TattooArtistService tattooArtistService;

    /**
     * @author Nanna
     * @return get client to create-new-profile view
     */
    @GetMapping("/create-new-profile")
    public String newProfile( Model model, HttpSession session) {
        byte[] imageData = (byte[]) session.getAttribute("imageData");
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
            System.out.println("here");
        }
        return "home/create-new-profile";
    }
    @PostMapping("upload-profile-picture")
    public String uploadProfilePicture(@RequestParam MultipartFile profilePicture,Model model, HttpSession session) throws IOException {
        if (!profilePicture.isEmpty()) {
            byte[] imageData = profilePicture.getBytes();
            session.setAttribute("imageData", imageData);
            System.out.println("now here");
        }
        return "redirect:/create-new-profile";
    }


    /**
     * @author Nanna
     * @param profileUsername variables used to create a profile
     * @param profileFirstname
     * @param profileLastName
     * @param profilePassword
     * @param email
     * @param phone
     * @param facebookUrl
     * @param instagramUrl
     * @param avgWorkHours
     * @param isAdmin
     * @param session used to check if the user is logged in, and check that the user is an admin
     * @return sends information to service to create a profile and returns to manage-profiles page
     */
    @PostMapping("/create-new-profile")
    public String createProfile(Model model, @RequestParam String profileUsername,
                                @RequestParam String profileFirstname,
                                @RequestParam String profileLastName,
                                @RequestParam String profilePassword,
                                @RequestParam String email,
                                @RequestParam int phone,
                                @RequestParam String facebookUrl,
                                @RequestParam String instagramUrl,
                                @RequestParam int avgWorkHours,
                                @RequestParam(value = "isAdmin", required = false) Boolean isAdmin
                               /* @RequestParam(value = "imageFile",required = false) byte[] imageData,*/, HttpSession session) {
        TattooArtist existingProfile = tattooArtistService.getTattooArtistByUsername(profileUsername);


        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);


        byte[] imageData = (byte[]) session.getAttribute("imageData");

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";

        }
        if (existingProfile != null) {
            model.addAttribute("message", "This username is taken");
            return "home/create-new-profile"; // Returns the same page with the message
        } else {

            boolean adminStatus = isAdmin != null && isAdmin;
            tattooArtistService.createProfile(profileUsername, profileFirstname, profileLastName, profilePassword, facebookUrl, instagramUrl, phone, email, avgWorkHours, adminStatus, Optional.ofNullable(imageData));
            return "redirect:/manage-profiles";
        }
    }


    /**
     * @author Nanna
     * @param session used to determine if the user is logged in and an admin
     * @param profileToDelete not required, used to send to delete endPoint that will find and delete in the database
     * @param message information displayed to user, both regarding admin status and deletion status
     * @return view with list of profiles, if profileToDelete is present, present warning
     */
    @GetMapping("/manage-profiles")
        public String manageProfiles(Model model, HttpSession session, @RequestParam(required = false) String profileToDelete, @RequestParam(required = false) String message){
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        if (profileToDelete != null) {
            model.addAttribute("profileToDelete", profileToDelete);
        }

        if (message != null) {
            model.addAttribute("message", message);
        }
        List<TattooArtist> profiles = tattooArtistService.showTattooArtist();
        model.addAttribute("profiles", profiles);
        model.addAttribute("user", tattooArtist);


        return "home/manage-profiles";
        }

    /**
     * @author Nanna
     * @param profileToDelete
     * @param redirectAttributes
     * @param session used to determine if user is logged in and admin
     * @return
     * actually noy necessary, should probably be fused with the one above
     */
    @PostMapping("/manage-profiles")
    public String manageProfilesWithWarning(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        redirectAttributes.addAttribute("profileToDelete", profileToDelete);
        return "redirect:/manage-profiles";
    }


    /**
     * @author Nanna
     * @param profileToDelete used to find tattooArtist in database to delete.
     * @param redirectAttributes sends a message to manage-profiles view to inform the user
     *                           of the outcome of the deletion
     * @param session used to determine if user is logged in and admin
     * @return deletes profile from database and returns to manage-profiles view.
     */
    @PostMapping("/delete-profile")
    public String deleteProfile(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        String message = tattooArtistService.deleteProfileByUsername(profileToDelete);
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/manage-profiles";
    }


    /**
     * @author
     * @param profileToChange used to find the profile that should change status in the database
     * @param redirectAttributes sends message to the user to inform them of
     *                           the outcome of the change in status.
     * @param session used to determine if the user is logged in and admin
     * @return change admin status of profile and return to manage-profile view
     */
    @PostMapping("/change-admin")
    public String changeAdmin(@RequestParam String profileToChange, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        TattooArtist artist = tattooArtistService.getTattooArtistByUsername(profileToChange);
        String message = tattooArtistService.changeAdminStatus(artist);
        redirectAttributes.addAttribute("message", message);
        return "redirect:/manage-profiles";
    }


  /*
    @PostMapping("/upload-profile-picture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "home/create-new-profile"; // Return to the create-new-profile page
        }


        try {
            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            // Save the file to the upload directory
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, bytes);

            // Pass the file path to the view for preview
            model.addAttribute("imageUrl", "/uploads/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload file");
            return "home/create-new-profile"; // Return to the create-new-profile page
        }

        // Redirect back to the create-new-profile page
        return "redirect:/create-new-profile";
        }
   */

}


