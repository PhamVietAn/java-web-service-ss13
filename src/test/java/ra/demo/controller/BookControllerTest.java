package ra.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ra.demo.advice.BookControllerAdvice;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.service.BookService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@Import(BookControllerAdvice.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void getAllBooks_returns200AndList() throws Exception {
        Book b1 = Book.builder().id(1L).title("A").author("X").category("C").quantity(1).build();
        Book b2 = Book.builder().id(2L).title("B").author("Y").category("D").quantity(2).build();
        when(bookService.getBooks()).thenReturn(Arrays.asList(b1, b2));

        mockMvc.perform(get("/api/v1/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    public void getBookById_found_returns200() throws Exception {
        Book b = Book.builder().id(1L).title("A").author("X").category("C").quantity(1).build();
        when(bookService.getBookById(1L)).thenReturn(b);

        mockMvc.perform(get("/api/v1/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("A"));
    }

    @Test
    public void getBookById_notFound_returns404() throws Exception {
        when(bookService.getBookById(100L)).thenThrow(new BookNotFound("Không tồn tại sách có mã 100"));

        mockMvc.perform(get("/api/v1/books/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

