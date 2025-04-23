package service.api

import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/books")
class BooksController(
    private val collection: BooksCollection
) {

    @GetMapping("/{id}", produces = [HAL_JSON_VALUE])
    fun getBookById(
        authentication: Authentication,
        @PathVariable id: UUID
    ): ResponseEntity<BookRepresentation> {
        val book = collection.getBook(id) ?: return notFound().build()
        if (userCanDelete(authentication, book)) {
            book.add()
        }
        return ok(book)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deleteBookById(
        authentication: Authentication,
        @PathVariable id: UUID,
        @RequestParam signature: String
    ) {
        collection.deleteBook(id)
    }

    private fun userCanDelete(authentication: Authentication, book: BookRepresentation): Boolean {
        // dummy implementation
        // here you actually check if the conditions are met to delete the book
        return true
    }
}

@Service
class BooksCollection {

    fun getBook(id: UUID): BookRepresentation? {
        TODO()
    }

    fun deleteBook(id: UUID) {
        TODO()
    }
}

data class BookRepresentation(
    val id: UUID,
    val title: String
    // ... more properties
) : RepresentationModel<BookRepresentation>()
