package com.ajackus.Library_Management_System.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler()) // Apply the exception handler
                .build();
    }

    @Test
    public void testHandleLibraryManagementException() throws Exception {
        mockMvc.perform(get("/test/library-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("This is a Library Management Exception")));
    }

    @Test
    public void testHandleGeneralException() throws Exception {
        mockMvc.perform(get("/test/general-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("An unexpected error occurred")));
    }

    @Test
    public void testHandleOptimisticLockingFailureException() throws Exception {
        mockMvc.perform(get("/test/optimistic-locking-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Conflict: The book was updated or deleted by another transaction.")));
    }

    /**
     * A simple test controller to trigger exceptions for testing.
     */
    @RestController
    @RequestMapping("/test")
    private static class TestController {

        @GetMapping("/library-exception")
        public void throwLibraryManagementException() {
            throw new LibraryManagementException("This is a Library Management Exception");
        }

        @GetMapping("/general-exception")
        public void throwGeneralException() {
            throw new RuntimeException("Unexpected error");
        }

        @GetMapping("/optimistic-locking-exception")
        public void throwOptimisticLockingFailureException() {
            throw new ObjectOptimisticLockingFailureException("Book", "1");
        }
    }
}
