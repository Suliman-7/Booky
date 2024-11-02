export async function fetchBookByISBN(isbn) {
    console.log("http://localhost:8080/api/v1/book/books/0-061-96436-0");
    console.log(isbn);
    const response = await fetch(`http://localhost:8080/api/v1/book/books/${isbn}`, {
        method: 'POST',
      });
    if (!response.ok) {
      throw new Error('Failed to fetch book data');
    }
    return await response.json();
  }

