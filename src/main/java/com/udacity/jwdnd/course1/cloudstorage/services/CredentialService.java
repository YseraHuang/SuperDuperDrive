package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.findById(credentialId);
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        return credentialMapper.getByUserId(userId);
    }

    public void createCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encodedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credentialMapper.insert(new Credential(null,credential.getUrl(), credential.getUsername(), encodedKey, encodedPassword, credential.getUserId()));
    }

    public void updateCredential(Credential credential) {
        //String encodedPassword = encryptionService.encryptValue(credential.getPassword(),credential.getKey());
        credentialMapper.update(new Credential(credential.getCredentialId(),credential.getUrl(),credential.getUsername(),credential.getKey(), credential.getPassword(), credential.getUserId()));
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }
}
