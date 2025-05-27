package br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.user.application.service.UserService;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserRequest;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserUseCaseImplTest {

    private UserService userService;
    private CreateUserUseCaseImpl createUserUseCase;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        createUserUseCase = new CreateUserUseCaseImpl(userService);
    }

    @Test
    void createUser_shouldDelegateToUserServiceAndReturnResponse() {
        CreateUserRequest request = mock(CreateUserRequest.class);
        CreateUserResponse expectedResponse = mock(CreateUserResponse.class);

        when(request.fullname()).thenReturn("Victor Pizzaia");
        when(request.cpf()).thenReturn("12345678910");
        when(request.email()).thenReturn("victor@email.com");
        when(request.plainPassword()).thenReturn("password");
        when(userService.createUser("Victor Pizzaia", "12345678910", "victor@email.com", "password"))
                .thenReturn(expectedResponse);

        CreateUserResponse actualResponse = createUserUseCase.createUser(request);

        assertSame(expectedResponse, actualResponse);
        verify(userService).createUser("Victor Pizzaia", "12345678910", "victor@email.com", "password");
    }
}
