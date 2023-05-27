package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final UserService userService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, Note note, Model model) { //we need to load the user data out as well
        //Load User Notes
        User user = userService.getUser(authentication.getName());

        List<Note> Notes = this.noteService.getAllUserNotes(user.getUserId());
        model.addAttribute("Notes", Notes);

        List<File> Files = fileService.getFilesByUserId(user.getUserId());
        model.addAttribute("Files", Files);

        List<Credential> Credentials = credentialService.getCredentialsByUserId(user.getUserId());
        model.addAttribute("Credentials",Credentials);

        return "home";
    }


    //Files Controller

    @PostMapping("/createFile")
    public String createFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model) { //@RequestParam("fileUpload") binds the file input name

        if (multipartFile.isEmpty()) {
            // Create and configure the alert
            JOptionPane.showMessageDialog(null, "Hello, World!");
            //model.addAttribute("error", "Cannot upload the file. Please select a file.");
            // Redirect back to the page or return an appropriate response
            return "redirect:/home";
        }

        // The reason of not directly using File to interact with frontend is that the form does not have match input field with appropriate name to the File class
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        // Extract the necessary information from the uploaded file
        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        long fileSize = multipartFile.getSize();
        try {
            byte[] fileData = multipartFile.getBytes();
            // Create a new File object with the extracted information
            File file = new File(null, fileName, contentType, String.valueOf(fileSize), userId, fileData);
            fileService.createFile(file);
            List<File> Files = fileService.getFilesByUserId(file.getUserId());
            model.addAttribute("Files", Files);
            return "redirect:/home";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam("fileId") Integer fileId) {
        fileService.deleteFile(fileId);
        return "redirect:/home";
    }

    @GetMapping("/viewFile")
    public ResponseEntity<Resource> viewFile(@RequestParam("fileId") Integer fileId) throws IOException {
        // Retrieve the file based on the fileId
        File file = fileService.getFileById(fileId);

        // Create the Resource object with the file data
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        // Set the appropriate Content-Type header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getFileData().length)
                .body(resource);
    }


    //Notes Controller
    @PostMapping("/createUpdateNote")
    public String createNote(Authentication authentication, Note note, Model model) {
        User user = userService.getUser(authentication.getName());
        note.setUserId(user.getUserId());

        if (noteService.findNote(note.getNoteId()) == null) {
            this.noteService.createNote(note);
        } else {
            noteService.updateNote(note);
        }

        List<Note> Notes = this.noteService.getAllUserNotes(note.getUserId());
        model.addAttribute("Notes", Notes);
        // cannot use redirect. Since it will be a new request. the model attribute data will be lost (Use redirect only when /home get request handle all updates)
        return "redirect:/home";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam("noteId") Integer noteId) { //@RequestParam("noteId") bind a request parameter (URL param)
        noteService.deleteNote(noteId);
        return "redirect:/home";
    }

    // Credential Controller
    @PostMapping("/createUpdateCredential")
    public String createUpdateCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        //@ModelAttribute bind the form fields to the corresponding fields in the Credential object. It is important since there is no input for key. if we do not bind, key will be missing
        User user = userService.getUser(authentication.getName());
        credential.setUserId(user.getUserId());

        if (credentialService.getCredentialById(credential.getCredentialId()) == null) {
            credentialService.createCredential(credential);
        } else {
            credentialService.updateCredential(credential);
        }

//        List<Credential> Credentials = credentialService.getCredentialsByUserId(credential.getUserId());
////        for (Credential credential1:Credentials) {
////            String decodedPassword = encryptionService.decryptValue(credential1.getPassword(), credential1.getKey());
////            credential1.setPassword(decodedPassword);
////        }
//        model.addAttribute("Credentials",Credentials);

        return "redirect:/home";
    }

    @GetMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId){
        credentialService.deleteCredential(credentialId);
        return "redirect:/home";
    }
}