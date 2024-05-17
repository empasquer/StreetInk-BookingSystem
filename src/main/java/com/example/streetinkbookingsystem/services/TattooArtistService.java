package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.TattooArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TattooArtistService {

    @Autowired
    private TattooArtistRepository tattooArtistRepository;

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

}
