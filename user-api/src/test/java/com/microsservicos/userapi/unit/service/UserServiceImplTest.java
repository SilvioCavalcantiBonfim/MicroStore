package com.microsservicos.userapi.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.exception.CpfAlreadyRegisteredException;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.userapi.model.User;
import com.microsservicos.userapi.repository.UserRepository;
import com.microsservicos.userapi.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private User user1 = new User();
  private User user2 = new User();
  private LocalDateTime register = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

  @BeforeEach
  public void setup() {
    user1.setId(1L);
    user1.setName("User 1");
    user1.setCpf("00000000000");
    user1.setAddress("address");
    user1.setEmail("user1@teste.com");
    user1.setPhone("000000000");
    user1.setRegister(register);
    user1.setKey("b5444221-6541-4a8a-99a2-4f8d8cc99222");

    user2.setId(2L);
    user2.setName("User 2");
    user2.setCpf("00000000001");
    user2.setEmail("user2@teste.com");
    user2.setAddress("address");
    user2.setPhone("000000000");
    user2.setRegister(register);
    user2.setKey("63cda5ff-2cb2-4fc1-8510-733b178fb70d");
  }

  @Test
  @DisplayName("Find all users successfully")
  public void findAllUserSuccessTest() {

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    List<UserOutputDto> output = userService.getAllUsers();

    assertThat(output)
        .extracting("name", "cpf", "address", "email", "phone", "register")
        .contains(
            new Tuple("User 1", "00000000000", "address", "user1@teste.com", "000000000", register),
            new Tuple("User 2", "00000000001", "address", "user2@teste.com", "000000000", register));

  }

  @Test
  @DisplayName("Query users by name successfully")
  public void queryByNameUserSuccessTest() {

    when(userRepository.queryByNameLike(any())).thenReturn(List.of(user1, user2));

    List<UserOutputDto> output = userService.findUsersByName("test");

    assertThat(output)
        .extracting("name", "cpf", "address", "email", "phone", "register")
        .contains(
            new Tuple("User 1", "00000000000", "address", "user1@teste.com", "000000000", register),
            new Tuple("User 2", "00000000001", "address", "user2@teste.com", "000000000", register));

  }

  @Test
  @DisplayName("Find user by ID successfully")
  public void findByIdUserSuccessTest() {

    Long id = 1L;

    when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user1));

    UserOutputDto output = userService.findUserById(id);

    assertThat(output).usingRecursiveComparison()
        .comparingOnlyFields("name", "cpf", "address", "email", "phone", "register").isEqualTo(user1);
  }

  @Test
  @DisplayName("Find user by ID - User not found")
  public void findByIdUserExceptionTest() {

    when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userService.findUserById(0L));
  }

  @Test
  @DisplayName("Save user successfully")
  public void saveUserSuccessTest() {

    when(userRepository.save(Mockito.any())).thenReturn(user1);

    UserOutputDto output = userService.createUser(
        new UserInputDto(user1.getName(), user1.getCpf(), user1.getAddress(), user1.getEmail(), user1.getPhone()));

    assertThat(output).usingRecursiveComparison()
        .comparingOnlyFields("name", "cpf", "address", "email", "phone", "register").isEqualTo(user1);
  }

  @Test
  @DisplayName("Save user - Duplicate CPF")
  public void saveUserWithDuplicatedCpfTest() {

    UserInputDto userDto = new UserInputDto("userDto", "00000000000", "address", "email@email.com", "900000000");

    when(userRepository.existsByCpf(any())).thenReturn(true);

    assertThatExceptionOfType(CpfAlreadyRegisteredException.class).isThrownBy(() -> userService.createUser(userDto));
  }

  @Test
  @DisplayName("Find user by CPF successfully")
  public void findByCpfUserSuccessTest() {

    when(userRepository.findByCpfAndKey(any(), any())).thenReturn(Optional.of(user1));

    UserOutputDto output = userService.findUserByCpf(user1.getCpf(), user1.getKey());

    assertThat(output).usingRecursiveComparison()
        .comparingOnlyFields("name", "cpf", "address", "email", "phone", "register").isEqualTo(user1);
  }

  @Test
  @DisplayName("Find user by CPF - Invalid CPF length")
  public void findByCpfUserInvalidLengthCpfTest() {

    assertThatExceptionOfType(InvalidCpfLengthException.class).isThrownBy(() -> userService.findUserByCpf("123", "123"));
  }

  @Test
  @DisplayName("Find user by CPF - User not found")
  public void findByCpfUserWithUserNotFoundTest() {

    when(userRepository.findByCpfAndKey(any(), any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.findUserByCpf(user1.getCpf(), user1.getKey()));
  }

  @Test
  @DisplayName("Delete user successfully")
  public void deleteUserSuccessTest() {

    when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user1));

    assertThatNoException().isThrownBy(() -> userService.deleteUser(user1.getId()));
  }

  @Test
  @DisplayName("Delete user - User not found")
  public void deleteUserExceptionTest() {

    when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userService.deleteUser(0L));
  }

}
