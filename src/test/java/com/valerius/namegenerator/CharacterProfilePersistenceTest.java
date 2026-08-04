package com.valerius.namegenerator;

import com.valerius.namegenerator.model.CharacterProfile;
import com.valerius.namegenerator.model.Gender;
import com.valerius.namegenerator.model.GeneratedResult;
import com.valerius.namegenerator.model.User;
import com.valerius.namegenerator.repository.CharacterProfileRepository;
import com.valerius.namegenerator.repository.GeneratedResultRepository;
import com.valerius.namegenerator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards against the File Away failure where remarks longer than the old
 * VARCHAR(255) background column were rejected by the database.
 */
@SpringBootTest
@Transactional
class CharacterProfilePersistenceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CharacterProfileRepository characterProfileRepository;
    @Autowired
    private GeneratedResultRepository generatedResultRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void savesProfileWithRemarksLongerThan255Characters() {
        User user = new User();
        user.setEmail("persistence-test@example.com");
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user = userRepository.save(user);

        String remarks = "Born 1979, a winemaker who took over the family vineyard after a career "
                + "as a sommelier in Lyon. Dry-humored, traditional, slightly formal. The name "
                + "should feel like a classic mid-century French name that was common for his "
                + "generation — not trendy — with a saint's given name and a rooted regional "
                + "surname. Include how the name would be shortened by friends.";
        assertThat(remarks.length()).isGreaterThan(255);

        CharacterProfile profile = new CharacterProfile();
        profile.setUser(user);
        profile.setGender(Gender.MALE);
        profile.setAge(47);
        profile.setBackground(remarks);
        profile = characterProfileRepository.saveAndFlush(profile);

        GeneratedResult result = new GeneratedResult();
        result.setProfile(profile);
        result.setGeneratedName("Jean-Baptiste Fabre");
        result.setNativeName("Jean-Baptiste Fabre");
        result.setExplanation("A classic mid-century French name rooted in Provençal Catholic tradition. "
                .repeat(5));
        assertThat(result.getExplanation().length()).isGreaterThan(255);
        result = generatedResultRepository.saveAndFlush(result);

        assertThat(characterProfileRepository.findById(profile.getId()))
                .get()
                .extracting(CharacterProfile::getBackground)
                .isEqualTo(remarks);
        assertThat(generatedResultRepository.findById(result.getId())).isPresent();
    }
}
