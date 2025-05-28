package br.com.victorpizzaia.wallet_service_assignment.user.web;

import br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.CreateUserUseCase;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserRequest;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserControllerTest {

    private UserController userController;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = mock(CreateUserUseCase.class);
        userController = new UserController(createUserUseCase);
    }

    @Test
    void createUserAndWallet_shouldReturnOkAndResponseBody() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("Victor Pizzaia", "12345678910", "victor@example.com", "password");

        ResponseEntity<CreateUserResponse> response = userController.createUserAndWallet(createUserRequest);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(createUserUseCase).createUser(createUserRequest);
    }
}
