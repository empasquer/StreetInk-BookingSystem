package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.TattooArtistRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class LoginService {


    private static final int SALT_LENGTH = 16;

    @Autowired
    private TattooArtistRepository tattooArtistRepository;

    @Autowired
    private HttpSession session;

    //Method that hashes and saves the passwords that are already in the database
    //Should be deleted when everyone have hashed the passwords on local database
    public void hashExistingPasswords() {
        List<TattooArtist> tattooArtists = tattooArtistRepository.showTattooArtists();
        for (TattooArtist tattooArtist : tattooArtists) {
            String plainPassword = tattooArtist.getPassword();
            String hashedPassword = hashPassword(plainPassword);
            tattooArtist.setPassword(hashedPassword);
            tattooArtistRepository.updatePassword(tattooArtist.getUsername(), tattooArtist.getPassword());
        }
    }

    //Basically just checks if the info matches the database with the hashed password this time
    public boolean authenticateUser(String username, String password) {
        TattooArtist tattooArtist = tattooArtistRepository.getTattooArtistByUsername(username);
        if (tattooArtist != null) {
            String storedPassword = tattooArtist.getPassword();
            if (verifyPassword(password, storedPassword)) {
                session.setAttribute("loggedIn", true); //Set "loggedIn" attribute in session
                session.setAttribute("username", username); //Set "username" attribute in session
                return true; //Authentication successful
            } else {
                return false; //Password mismatch
            }
        } else {
            return false; //User not found
        }
    }

    //Returns true if session attribute loggedIn is not null and loggedIn == true, else false
    //We cast loggedIn as boolean because session can return attributes as Objects.
    //Might need it in the header for the burgermenu
    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn");
    }

    //Salts and the hashes the password and the adds the salt again s it is stored with the salted and hashed password
    public String hashPassword(String password) {
        try {
            //Generates random characters - the salt part
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            //Combines salt with password and hashes it
            byte[] hashedPassword = saltAndHash(password, salt);

            //Combines the salt with the already hashed password, so we know what the salt is
            byte[] saltedHashedPassword = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltedHashedPassword, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltedHashedPassword, salt.length, hashedPassword.length);

            //Encodes the whole byte to a string,so it is saved like a string in database
            return Base64.getEncoder().encodeToString(saltedHashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Method to verify the given password with the stored hashed password
    public boolean verifyPassword(String password, String hashedPassword) {
        try {
            //Decodes and extracts the original salt, so we know what the salt is
            byte[] decodedHashedPassword = Base64.getDecoder().decode(hashedPassword);
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(decodedHashedPassword, 0, salt, 0, salt.length);

            //Using the original salt, we get the combined salt and password hashed again
            byte[] hashedPasswordToCompare = saltAndHash(password, salt);

            //This is the hashed password that is already stored in the database, starting right after the salt
            byte[] storedHash = new byte[decodedHashedPassword.length - salt.length];
            System.arraycopy(decodedHashedPassword, salt.length, storedHash, 0, storedHash.length);

            int diff = getDiff(hashedPasswordToCompare, storedHash);

            return diff == 0;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Compares the storedHash password with the one made with the given password and the salt
    private static int getDiff(byte[] hashedPasswordToCompare, byte[] storedHash) {
        //First line compares the bitwise length. If they are different, the byte by byte comparison won't happen
        //(^) means bitwise XOR - compares pairs of bits and if the both are 1 or 0 the result is 0, else it is 1
        //(|) bitwise OR - If wither of the bits are 1, the result is 1, else it is 0
        int diff = hashedPasswordToCompare.length ^ storedHash.length;

        for (int i = 0; i < hashedPasswordToCompare.length && i < storedHash.length; i++) {
            //diff takes the result of XOR and ORs it with current value of diff to keep track of differences
            diff |= hashedPasswordToCompare[i] ^ storedHash[i];
        }

        //If it's 0, there were no differences between the bytes and true is returned
        return diff;
    }

    //Method that uses the combined salt and password and hashes it
    private byte[] saltAndHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        //Combines the salt san password together
        byte[] saltedPassword = new byte[salt.length + password.getBytes().length];

        //Basically converts the salt and password to a single byte array before hashing
        //First line copies the bytes of the salt in the beginning in saltedPassword
        //Second line copies the bytes of the password and places it just after the salt
        System.arraycopy(salt, 0, saltedPassword, 0, salt.length);
        System.arraycopy(password.getBytes(), 0, saltedPassword, salt.length, password.getBytes().length);

        //Hashes the byte array using the chosen algorithm and digest (hashes) the whole thing
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(saltedPassword);
    }

    //Generates an 8 char random password for forgotten password
    public String randomPassword() {
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(validChars.length());
            password.append(validChars.charAt(index));
        }
        return password.toString();
    }





}
