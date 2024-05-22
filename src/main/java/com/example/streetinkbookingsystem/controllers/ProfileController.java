package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Controller
public class ProfileController {
    @Autowired
    TattooArtistService tattooArtistService;
    @Autowired
    LoginService loginService;

    @GetMapping("/profile")
    public String seeProfile(HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        session.removeAttribute("imageData");
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);

        //to display profile pic:
        if (tattooArtist.getProfilePicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(tattooArtist.getProfilePicture());
            tattooArtist.setBase64ProfilePicture(base64Image);
        }

        if (username == null) {
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        } else {
            return "home/profile";
        }

    }

    /**
     * @return get client to create-new-profile view
     * @author Nanna
     */
    @GetMapping("/create-new-profile")
    public String newProfile(Model model, HttpSession session) {
        //to display the preview if a picture is chosen
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        byte[] imageData = (byte[]) session.getAttribute("imageData");
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }
        return "home/create-new-profile";
    }





    @PostMapping("upload-profile-picture")
    public String uploadProfilePicture(@RequestParam MultipartFile profilePicture,
                                      @RequestParam(required = false) String action,

                                       HttpSession session) throws IOException {
        byte[] imageData = null;

        if (!profilePicture.isEmpty()) {
            imageData = profilePicture.getBytes();
            session.setAttribute("imageData", imageData);
            System.out.println("now here");
        }

        switch (action) {
            case "edit":
                return "redirect:/edit-profile";
            case "create":
                return "redirect:/create-new-profile";
            default:
                // Handle unexpected action values, if necessary
                return "redirect:/";
        }
    }


    /**
     * @param profileUsername  variables used to create a profile
     * @param profileFirstname
     * @param profileLastName
     * @param profilePassword
     * @param email
     * @param phone
     * @param facebookUrl
     * @param instagramUrl
     * @param avgWorkHours
     * @param isAdmin
     * @param session          used to check if the user is logged in, and check that the user is an admin
     * @return sends information to service to create a profile and returns to manage-profiles page
     * @author Nanna
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
          , HttpSession session) {
        TattooArtist existingProfile = tattooArtistService.getTattooArtistByUsername(profileUsername);

        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);


        byte[] imageData = (byte[]) session.getAttribute("imageData");

        if (username == null || !tattooArtist.getIsAdmin()) {
            // Redirect logic when username is null or if not admin.
            return "redirect:/";

        }
        if (existingProfile != null) {
            model.addAttribute("message", "This username is taken");
            return "home/create-new-profile"; // Returns the same page with the message
        } else {

            boolean adminStatus = isAdmin != null && isAdmin;
            tattooArtistService.createProfile(profileUsername, profileFirstname, profileLastName, profilePassword, facebookUrl, instagramUrl, phone, email, avgWorkHours, adminStatus, Optional.ofNullable(imageData));
            session.removeAttribute("imageData");
            return "redirect:/manage-profiles";
        }
    }


    /**
     * @param session         used to determine if the user is logged in and an admin
     * @param profileToDelete not required, used to send to delete endPoint that will find and delete in the database
     * @param message         information displayed to user, both regarding admin status and deletion status
     * @return view with list of profiles, if profileToDelete is present, present warning
     * @author Nanna
     */
    @GetMapping("/manage-profiles")
    public String manageProfiles(Model model, HttpSession session, @RequestParam(required = false) String profileToDelete, @RequestParam(required = false) String message) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()) {
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


        //Remove imageData so it doesn't reapear when creating a new profile
        session.removeAttribute("imageData");

        return "home/manage-profiles";
    }

    /**
     * @param profileToDelete
     * @param redirectAttributes
     * @param session            used to determine if user is logged in and admin
     * @return actually noy necessary, should probably be fused with the one above
     * @author Nanna
     */
    @PostMapping("/manage-profiles")
    public String manageProfilesWithWarning(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()) {
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        redirectAttributes.addAttribute("profileToDelete", profileToDelete);
        return "redirect:/manage-profiles";
    }


    /**
     * @param profileToDelete    used to find tattooArtist in database to delete.
     * @param redirectAttributes sends a message to manage-profiles view to inform the user
     *                           of the outcome of the deletion
     * @param session            used to determine if user is logged in and admin
     * @return deletes profile from database and returns to manage-profiles view.
     * @author Nanna
     */
    @PostMapping("/delete-profile")
    public String deleteProfile(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()) {
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        String message = tattooArtistService.deleteProfileByUsername(profileToDelete);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/manage-profiles";
    }


    /**
     * @param profileToChange    used to find the profile that should change status in the database
     * @param redirectAttributes sends message to the user to inform them of
     *                           the outcome of the change in status.
     * @param session            used to determine if the user is logged in and admin
     * @return change admin status of profile and return to manage-profile view
     * @author Nanna
     */
    @PostMapping("/change-admin")
    public String changeAdmin(@RequestParam String profileToChange, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()) {
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        TattooArtist artist = tattooArtistService.getTattooArtistByUsername(profileToChange);
        String message = tattooArtistService.changeAdminStatus(artist);
        redirectAttributes.addAttribute("message", message);
        return "redirect:/manage-profiles";
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model, HttpSession session) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        TattooArtist artist = tattooArtistService.getTattooArtistByUsername((String) session.getAttribute("username"));

        // To display preview if picture is chosen
        byte[] imageData = (byte[]) session.getAttribute("imageData");
        if (imageData != null) {
            String newBase64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("newBase64Image", newBase64Image);
        }


        return "home/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam String firstName, @RequestParam (required = false)String lastName,
                                @RequestParam String email, @RequestParam int phoneNumber,
                                @RequestParam (required = false) String facebook, @RequestParam (required = false) String instagram,
                                @RequestParam int avgWorkHours, @RequestParam String newUsername,
                                @RequestParam String currentUsername,
                                Model model, HttpSession session) {
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        byte[] imageData = (byte[]) session.getAttribute("imageData");

        tattooArtistService.updateTattooArtist(firstName, lastName, email, phoneNumber, facebook, instagram, avgWorkHours, newUsername, currentUsername,  Optional.ofNullable(imageData));
        session.setAttribute("username", newUsername);
        session.removeAttribute("imageData");
        return "redirect:/profile";
    }

    /**
     * @author Munazzah
     * @return String
     */
    @GetMapping("/reset-password")
        public String resetPassword () {
            return "home/reset-password";

    }

    /**
     * @author Munazzah
     * @param currentPassword
     * @param newPassword
     * @param repeatedPassword
     * @param session
     * @param model
     * @param redirectAttributes
     * @return String
     * @summary the method checks if the new password matches the repeated password and if
     * the current password matches the password in the database
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String currentPassword, @RequestParam String newPassword,
                                @RequestParam String repeatedPassword, HttpSession session, Model model,
                                RedirectAttributes redirectAttributes) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        String username = (String) session.getAttribute("username");
        String hashedPassword = tattooArtistService.getPassword(username);

        if (loginService.verifyPassword(currentPassword, hashedPassword)) {
            if (newPassword.equals(repeatedPassword)) {
                loginService.updatePassword(newPassword, username);
            } else {
                redirectAttributes.addFlashAttribute("message", "The two passwords don't match");
                return "redirect:/reset-password";
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Incorrect current password");
            return "redirect:/reset-password";
        }

        return "redirect:/login";
    }


}


