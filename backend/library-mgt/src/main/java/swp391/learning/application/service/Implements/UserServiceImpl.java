package swp391.learning.application.service.Implements;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swp391.learning.application.service.UserService;
import swp391.learning.domain.dto.request.user.authentication.UserRequest;
import swp391.learning.domain.dto.response.user.authentication.UserResponse;
import swp391.learning.domain.entity.User;
import swp391.learning.domain.enums.EnumTypeRole;
import swp391.learning.domain.enums.EnumUserStatus;
import swp391.learning.exception.DuplicateResourceException;
import swp391.learning.exception.ResourceNotFoundException;
import swp391.learning.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public List<UserResponse> getAllUserByRole() {
        List<User> users = userRepository.findAllByLibrarianOrMemberRoles(EnumTypeRole.MEMBER, EnumTypeRole.LIBRARIAN);
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());

    }

    @Override
    public void addUser(UserRequest userRequest) {
        String email = userRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email đã tồn tại");
        }

        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .role(EnumTypeRole.valueOf(userRequest.getRole()))
                .status(EnumUserStatus.valueOf(userRequest.getStatus()))
                .verified(false)
                .build();

        userRepository.save(user);
    }

    @Override
    public void updateUser(int id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(EnumTypeRole.valueOf(userRequest.getRole()));
        user.setStatus(EnumUserStatus.valueOf(userRequest.getStatus()));

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        return mapToUserResponse(user);
    }


    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus().toString())
                .role(user.getRole().name())
                .verified(user.getVerified())
                .build();
    }


}
