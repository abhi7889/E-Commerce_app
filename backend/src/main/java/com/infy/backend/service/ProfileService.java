package com.infy.backend.service;

import com.infy.backend.io.ProfileRequest;
import com.infy.backend.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);

    ProfileResponse getUserByEmail(String email);
}