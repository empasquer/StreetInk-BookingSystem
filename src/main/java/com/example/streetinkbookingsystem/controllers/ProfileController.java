package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Client;
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

    /**
     * @author Emma
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/profile")
    public String seeProfile(HttpSession session, Model model) {

        session.removeAttribute("imageData");
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        TattooArtist tattooArtist = loginService.addLoggedInUserInfo(model, session, tattooArtistService);


        //to display profile pic:
        if (tattooArtist.getProfilePicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(tattooArtist.getProfilePicture());
            tattooArtist.setBase64ProfilePicture(base64Image);
        }
        return "home/profile";


    }


    /**
     * @author Nanna
     * @param model used to add attributes for rendering the view.
     * @param session Used to retrieve user information.
     * @return The view name for the create-new-profile page.
     */
    @GetMapping("/create-new-profile")
    public String newProfile(Model model, HttpSession session) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        // Used to display a preview of a picture if one is uploaded
        byte[] imageData = (byte[]) session.getAttribute("imageData");
        if (imageData != null) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("base64Image", base64Image);
        }
        return "home/create-new-profile";
    }


    /**
     * @author Nanna
     * @param profilePicture The profile picture file uploaded by the user.
     * @param action  parameter indicating whether to edit or create a profile.
     * @param session used to store the uploaded image data.
     * @return A redirection based on the specified action, either create a new profile or edit a profile
     * @throws IOException if an error occurs while reading the uploaded file.
     */
    @PostMapping("upload-profile-picture")
    public String uploadProfilePicture(@RequestParam MultipartFile profilePicture,
                                      @RequestParam(required = false) String action,

                                       HttpSession session) throws IOException {
        byte[] imageData = null;
        if (!profilePicture.isEmpty()) {
            imageData = profilePicture.getBytes();
            session.setAttribute("imageData", imageData);
        }

        switch (action) {
            case "edit":
                return "redirect:/edit-profile";
            case "create":
                return "redirect:/create-new-profile";
            default:
                return "redirect:/";
        }
    }


    /**
     * @author Nanna
     * @param model used add attributes for rendering the view.
     * @param profileUsername The username of the new profile.
     * @param profileFirstname The first name of the new profile.
     * @param profileLastName The last name of the new profile.
     * @param profilePassword The password of the new profile.
     * @param email The email address of the new profile.
     * @param phone The phone number of the new profile.
     * @param facebookUrl The Facebook URL of the new profile.
     * @param instagramUrl The Instagram URL of the new profile.
     * @param avgWorkHours The average work hours per day of the new profile.
     * @param isAdmin A boolean indicating whether the new profile is an admin.
     * @param session to check if the user is logged in and to retrieve uploaded image data.
     * @return redirects to the manage-profiles page after creating the new profile.
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


        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        TattooArtist tattooArtist = loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        if (!tattooArtist.getIsAdmin()) {
            // Redirect if not admin.
            return "redirect:/";

        }
        //to display profile pic
        byte[] imageData = (byte[]) session.getAttribute("imageData");
        //Check if username is taken
        TattooArtist existingProfile = tattooArtistService.getTattooArtistByUsername(profileUsername);
        if (existingProfile != null) {
            model.addAttribute("message", "This username is taken");
            return "home/create-new-profile"; // Returns the same page with the message
        } else {
            boolean adminStatus = isAdmin != null && isAdmin; //convert null Boolean to false if needed
            tattooArtistService.createProfile(profileUsername, profileFirstname, profileLastName, profilePassword, facebookUrl, instagramUrl, phone, email, avgWorkHours, adminStatus, Optional.ofNullable(imageData));
            session.removeAttribute("imageData"); // remove the image data when done so that it doesn't appear when creating a new profile
            return "redirect:/manage-profiles";
        }
    }


    /**
     * @authot Nanna
     * @summary  retrieves the list of profiles that can be deleted or change admin status
     * * @param model  add attributes to for rendering the view
     *  * @param session  to check user login status
     *  * @param profileToDelete the username of the profile to delete, if any
     *  * @param message   an optional message to display,used to confirm profile deletion or admin status changes
     *  * @return the view for managing profiles

     */
    @GetMapping("/manage-profiles")
    public String manageProfiles(Model model, HttpSession session, @RequestParam(required = false) String profileToDelete, @RequestParam(required = false) String message) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        TattooArtist tattooArtist = loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        if (!tattooArtist.getIsAdmin()) {
            // Redirect if not admin.
            return "redirect:/";
        }
        //If deleting profile
        if (profileToDelete != null) {
            model.addAttribute("profileToDelete", profileToDelete);
        }
        //Message depending on if deleting user or changing admin status
        if (message != null) {
            model.addAttribute("message", message);
        }
        //to display list of profiles
        List<TattooArtist> profiles = tattooArtistService.showTattooArtist();
        model.addAttribute("profiles", profiles);


        //Remove imageData so it doesn't reapear when creating a new profile
        session.removeAttribute("imageData");

        return "home/manage-profiles";
    }

    /**
     * @author Nanna
     *  @param profileToDelete      the username of the profile to delete
     *  @param redirectAttributes   redirect messages to confirm deletion
     *  @param session              check user login status
     *  @param model                to add attributes to for rendering the view
     *  @return the view for manage profiles
     */
    @PostMapping("/delete-profile")
    public String deleteProfile(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        TattooArtist tattooArtist = loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        if (!tattooArtist.getIsAdmin()) {
            // Redirect if not admin.
            return "redirect:/";
        }
        String message = tattooArtistService.deleteProfileByUsername(profileToDelete);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/manage-profiles";
    }


    /**
     *
     * @author Nanna
     *  @param profileToChange      the username of the profile to change admin status
     *  @param redirectAttributes   redirect messages to confirm change
     *  @param session              check user login status
     *  @param model                to add attributes to for rendering the view
     *  @return the view for manage profiles
     */
    @PostMapping("/change-admin")
    public String changeAdmin(@RequestParam String profileToChange, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        TattooArtist tattooArtist = loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        if (!tattooArtist.getIsAdmin()) {
            // Redirect if not admin.
            return "redirect:/";
        }
        String message = tattooArtistService.changeAdminStatus(profileToChange);
        redirectAttributes.addAttribute("message", message);
        return "redirect:/manage-profiles";
    }

    /**
     * @author Nanna
     *  @param session              check user login status
     *  @param model                to add attributes to for rendering the view
     * @return view for editing a profile for session username
     */
    @GetMapping("/edit-profile")
    public String editProfile(Model model, HttpSession session) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        // To display preview if picture is chosen
        byte[] imageData = (byte[]) session.getAttribute("imageData");
        if (imageData != null) {
            String newBase64Image = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("newBase64Image", newBase64Image);
        }

        return "home/edit-profile";
    }

    /**
     * @author Nanna
     * @param firstName        the first name of the tattoo artist
     * @param lastName         the last name of the tattoo artist (optional)
     * @param email            the email address of the tattoo artist
     * @param phoneNumber      the phone number of the tattoo artist
     * @param facebook         the Facebook URL of the tattoo artist (optional)
     * @param instagram        the Instagram URL of the tattoo artist (optional)
     * @param avgWorkHours     the average work hours of the tattoo artist
     * @param newUsername      the new username for the tattoo artist
     * @param currentUsername  the current username of the tattoo artist
     * @param model            to add attributes to for rendering the view
     * @param session          to check user login status
     * @return the view name for the profile page after updating the profile
     */

    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam String firstName, @RequestParam(required = false) String lastName,
                                @RequestParam String email, @RequestParam int phoneNumber,
                                @RequestParam(required = false) String facebook, @RequestParam(required = false) String instagram,
                                @RequestParam int avgWorkHours, @RequestParam String newUsername,
                                @RequestParam String currentUsername,
                                Model model, HttpSession session) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        byte[] imageData = (byte[]) session.getAttribute("imageData");

        tattooArtistService.updateTattooArtist(firstName, lastName, email, phoneNumber, facebook, instagram, avgWorkHours, newUsername, currentUsername, Optional.ofNullable(imageData));
        // Update the session with the new username and remove image data
        session.removeAttribute("imageData");
        if (newUsername != null) {
            session.setAttribute("username", newUsername);
        }

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


