package swp391.learning.application.service;

import swp391.learning.domain.dto.common.ResponseCommon;
import swp391.learning.domain.dto.request.user.authentication.*;
import swp391.learning.domain.dto.response.user.authentication.*;
import swp391.learning.domain.entity.User;
import swp391.learning.security.jwt.JWTResponse;

public interface AuthenticationService {
    ResponseCommon<CreateUserResponseDTO> createUser(CreateUserRequest requestDTO);

    String getUserFromEmail(String email);
//    ResponseCommon<GetUserByEmailResponse> getUserByEmail(GetUserByEmailRequest getUserByEmailRequest);

    ResponseCommon<JWTResponse> login(LoginRequest loginRequest);
    ResponseCommon<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseCommon<VerifyOtpResponse> verifyOtp(VerifyOtpRequest verifyOtpRequest);

    User updateUser(User user);

    ResponseCommon<GetOTPResponse> getOtp(GetOTPRequest request);

    ResponseCommon<ResendOTPResponse> resendOTP(ResendOTPRequest request);

    ResponseCommon<SetRoleUserResponse> setRole(SetRoleUserRequest setRoleUserRequest);
}
