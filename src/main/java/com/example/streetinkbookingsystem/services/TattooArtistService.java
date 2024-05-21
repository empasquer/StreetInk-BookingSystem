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
     * @author Tara
     * @return viser en liste med alle tatovørene
     */
    public List<TattooArtist> showTattooArtist(){
        return tattooArtistRepository.showTattooArtists();
    }

    public TattooArtist getTattooArtistByUsername(String username) {
        return tattooArtistRepository.getTattooArtistByUsername(username);
    }

    public String deleteProfileByUsername(String profileToDelete) {
        TattooArtist artist = tattooArtistRepository.getTattooArtistByUsername(profileToDelete);
        if(artist.getIsAdmin()) {
            return "Cannot delete an admin profile";
        }
        else tattooArtistRepository.deleteProfileByUsername(profileToDelete);
        return artist.getUsername() + "deleted";
    }

    public String changeAdminStatus(TattooArtist artist) {
        boolean isAdmin = artist.getIsAdmin();
        List<TattooArtist> artists = tattooArtistRepository.showTattooArtists();
        int adminCount = 0;

        for (TattooArtist currentArtist : artists) {
            if (currentArtist.getIsAdmin()) {
                adminCount++;
            }
        }

        if (!isAdmin) {
            tattooArtistRepository.changeAdminStatus(artist.getUsername(), true);
            return "Admin status granted";
        } else {
            if (adminCount > 1) {
                tattooArtistRepository.changeAdminStatus(artist.getUsername(), false);
                return "Admin status revoked";
            } else {
                return "At least 1 admin required";
            }
        }
    }


    public void createProfile(String username, String firstname, String lastName, String password, String facebookUrl,  String instagramUrl, int phone, String email, int avgWorkHours, boolean isAdmin, Optional<byte[]> pictureData){
        String hashedPassword = loginService.hashPassword(password);
        tattooArtistRepository.createProfile(username,firstname,lastName,hashedPassword,facebookUrl,instagramUrl,phone, email, avgWorkHours, isAdmin,pictureData);
    }

    public void updateTattooArtist(String firstName, String lastName, String email, int phoneNumber, String facebook, String instagram, int avgWorkHours, String newUsername, String currentUsername, Optional<byte[]> pictureData) {
    tattooArtistRepository.updateTattooArtist(firstName,lastName,email,phoneNumber,firstName,instagram,avgWorkHours,newUsername, currentUsername, pictureData);
}
    public String getPassword(String username) {
        return tattooArtistRepository.getPassword(username);
    }

    /**
     * @author Munazzah
     * @param username
     * @return String
     */
    public String getPassword(String username) {
        return tattooArtistRepository.getPassword(username);
    }
}
