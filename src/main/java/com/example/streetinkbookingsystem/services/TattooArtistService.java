package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.TattooArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TattooArtistService {

    @Autowired
    private TattooArtistRepository tattooArtistRepository;
    @Autowired
    private LoginService loginService;

    /**
     * Retrieves a list of all tattoo artists.
     *
     * @return a list containing all tattoo artists
     * @Author Tara
     */
    public List<TattooArtist> showTattooArtist(){
        return tattooArtistRepository.showTattooArtists();
    }

    /**
     * @author Emma
     * @param username of the tattooArist to fetch
     * @return the tattooArtist
     */
    public TattooArtist getTattooArtistByUsername(String username) {
        return tattooArtistRepository.getTattooArtistByUsername(username);
    }

    /**
     * @author Nanna
     *@param profileToDelete The username of the profile to be deleted
     * @return A message indicating the result of the deletion
     */
    public String deleteProfileByUsername(String profileToDelete) {
        TattooArtist artist = tattooArtistRepository.getTattooArtistByUsername(profileToDelete);
        if(artist.getIsAdmin()) {
            return "Cannot delete an admin profile";
        }
        else tattooArtistRepository.deleteProfileByUsername(profileToDelete);
        return artist.getUsername() + " deleted";
    }

    /**
     * @author Nanna
     * @param tattooArtist The username of the tattoo artist whose admin status is to be changed
     * @return A message indicating change in status
     */
    public String changeAdminStatus(String tattooArtist) {
        TattooArtist artist = tattooArtistRepository.getTattooArtistByUsername(tattooArtist);
        boolean isAdmin = artist.getIsAdmin();

        List<TattooArtist> artists = tattooArtistRepository.showTattooArtists();

        int adminCount = 0;
        for (TattooArtist currentArtist : artists) {
            if (currentArtist.getIsAdmin()) {
                adminCount++;
            }
        }

        // If the tattoo artist is not an admin
        if (!isAdmin) {
            // Grant admin status to the tattoo artist
            tattooArtistRepository.changeAdminStatus(artist.getUsername(), true);
            return "Admin status granted";
        } else {
            // If there is more than one admin artist, revoke admin status from the tattoo artist
            if (adminCount > 1) {
                tattooArtistRepository.changeAdminStatus(artist.getUsername(), false);
                return "Admin status revoked";
            } else {
                // If there is only one admin artist, indicate that at least one admin is required
                return "At least 1 admin required";
            }
        }
    }

    /**
     * @author Nanna
     * @param username The username of the tattoo artist
     * @param firstName The first name of the tattoo artist
     * @param lastName The last name of the tattoo artist
     * @param password The password of the tattoo artist
     * @param facebook The Facebook URL of the tattoo artist
     * @param instagram The Instagram URL of the tattoo artist
     * @param phoneNumber The phone number of the tattoo artist
     * @param email The email address of the tattoo artist
     * @param avgWorkHours The average work hours per day of the tattoo artist
     * @param isAdmin A boolean indicating whether the tattoo artist is an admin
     * @param pictureData Optional profile picture data of the tattoo artist
     */
    public void createProfile(String username, String firstName, String lastName, String password, String facebook,  String instagram, int phoneNumber, String email, int avgWorkHours, boolean isAdmin, Optional<byte[]> pictureData){
        String hashedPassword = loginService.hashPassword(password);
        tattooArtistRepository.createProfile(username,firstName,lastName,hashedPassword,facebook,instagram,phoneNumber, email, avgWorkHours, isAdmin,pictureData);
    }

    /**
     * @author Nanna
     * @param currentUsername The current username of the tattoo artist
     * @param firstName The first name of the tattoo artist
     * @param lastName The last name of the tattoo artist
     * @param facebook The Facebook URL of the tattoo artist
     * @param instagram The Instagram URL of the tattoo artist
     * @param phoneNumber The phone number of the tattoo artist
     * @param email The email address of the tattoo artist
     * @param avgWorkHours The average work hours per day of the tattoo artist
     * @param pictureData Optional profile picture data of the tattoo artist
     */
    public void updateTattooArtist(String firstName, String lastName, String email, int phoneNumber, String facebook, String instagram, int avgWorkHours, String newUsername, String currentUsername, Optional<byte[]> pictureData) {
    tattooArtistRepository.updateTattooArtist(firstName,lastName,email,phoneNumber,facebook,instagram,avgWorkHours,newUsername, currentUsername, pictureData);
}

    /**
     * @author Munazzah
     * @param username To get the right password
     * @return String the password
     */
    public String getPassword(String username) {
        return tattooArtistRepository.getPassword(username);
    }
}
