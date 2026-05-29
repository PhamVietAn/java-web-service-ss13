package ra.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookResporitory bookResporitory;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void getAllBooks_returnList() {
        Book b1 = Book.builder().id(1L).title("A").author("X").category("C").quantity(1).build();
        Book b2 = Book.builder().id(2L).title("B").author("Y").category("D").quantity(2).build();
        when(bookResporitory.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<Book> result = bookService.getBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookResporitory, times(1)).findAll();
    }

    @Test
    public void getBookById_found() {
        Book b = Book.builder().id(1L).title("A").author("X").category("C").quantity(1).build();
        when(bookResporitory.findById(1L)).thenReturn(Optional.of(b));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookResporitory, times(1)).findById(1L);
    }

    @Test
    public void getBookById_notFound() {
        when(bookResporitory.findById(100L)).thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, () -> bookService.getBookById(100L));
        verify(bookResporitory, times(1)).findById(100L);
    }
}

