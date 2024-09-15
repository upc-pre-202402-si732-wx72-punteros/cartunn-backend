package com.thecoders.cartunnbackend.iam.infrastructure.hashing.bcrypt;

import com.thecoders.cartunnbackend.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
