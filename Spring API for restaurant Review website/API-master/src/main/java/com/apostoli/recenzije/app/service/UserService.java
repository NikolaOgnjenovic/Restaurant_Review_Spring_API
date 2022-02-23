package com.apostoli.recenzije.app.service;

import com.apostoli.recenzije.app.exceptions.ReviewNotFound;
import com.apostoli.recenzije.app.model.ReturnReviewDto;
import com.apostoli.recenzije.app.repository.UserRepository;
import com.apostoli.recenzije.app.model.ReturnUserDto;
import com.apostoli.recenzije.app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository UserRepository;
    private final ReviewService reviewService;

    @Autowired
    private JavaMailSender mailSender;

    public List<ReturnUserDto> getAllUsers() {
        return UserRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ReturnUserDto getUserById(Long id) {
        Optional<User> optionalUser = UserRepository.findById(id);

        if (optionalUser.isPresent()) {
            return mapToDto(optionalUser.get());
        } else {
            throw new ReviewNotFound("Requested id not present [" + id + "]");
        }
    }

    public ReturnUserDto getUserByUsername(String username) {
        List<ReturnUserDto> allUsers = getAllUsers();
        for(ReturnUserDto user : allUsers) {
            if(user.username().equals(username))
                return user;
        }

        return null;
    }

    public boolean validateLogin(String username, String password) {
        List<ReturnUserDto> users = getAllUsers();
        for(ReturnUserDto user : users) {
            if(Objects.equals(user.username(), username) && Objects.equals(user.password(), password))
                return true;
        }

        return false;
    }

    public ReturnUserDto createUser(User user) {
        try {
            return mapToDto(UserRepository.save(user));
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create duplicates");
        }
    }

    public int getUserLikeCount(String email) {
        List<ReturnReviewDto> allReviews = reviewService.getAllReviews();
        int likes = 0;
        for (ReturnReviewDto review : allReviews) {
            if(review.userEmail().equals(email)) {
                likes += review.likes();
            }
        }
        return likes;
    }

    public int getUserDislikeCount(String email) {
        List<ReturnReviewDto> allReviews = reviewService.getAllReviews();
        int dislikes = 0;
        for (ReturnReviewDto review : allReviews) {
            if(review.userEmail().equals(email)) {
                dislikes += review.dislikes();
            }
        }
        return dislikes;
    }

    public List<ReturnReviewDto> getReviewsByUserEmail(String email) {
        List<ReturnReviewDto> allReviews = reviewService.getAllReviews();
        allReviews.removeIf(review -> !Objects.equals(review.userEmail(), email));

        return allReviews;
    }

    public void forgotPassword(String email) {
        List<ReturnUserDto> allUsers = getAllUsers();

        String username = "", password = "";
        for(ReturnUserDto user : allUsers) {
            if(user.email().equals(email)) {
                username = user.username();
                password = user.password();
                break;
            }
        }
        String emailBody = "Na ovaj imejl poslat je zahtev za pregled korisnickog imena i sifre za vebsajt 'Hrana blizu vas'.\n" +
                "Vase korisnicko ime je: " + username + "\nVasa sifra je: " + password + "\nAko Vi niste zahtevali ove podatke, ignorisite i izbrisite ovaj imejl.";
        sendEmail(email, emailBody);
    }

    private void sendEmail(String toEmail, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("zenyattadragon@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject("Hrana u blizini ime i sifra");

            mailSender.send(message);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteUserById(Long id) {
        Optional<User> optionalUser = UserRepository.findById(id);

        if (optionalUser.isPresent()) {
            UserRepository.deleteById(id);
        } else {
            throw new ReviewNotFound("Requested id not present [" + id + "]");
        }
    }

    private ReturnUserDto mapToDto(User user) {
        return new ReturnUserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
    }
}
