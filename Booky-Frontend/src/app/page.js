'use client';
import { fetchBookByISBN } from '@/lib/api';
import { useState } from 'react';

export default function BookReadingListManager() {
  const [isbn, setIsbn] = useState('');
  const [books, setBooks] = useState([]);
  const [readingListName, setReadingListName] = useState('');
  const [readingLists, setReadingLists] = useState([]);
  const [bookIds, setBookIds] = useState({});
  const [readingListBooks, setReadingListBooks] = useState({});


  const handleAddBook = async () => {
    if (isbn && !books.some(book => book.isbn === isbn)) {
      let book = await fetchBookByISBN(isbn);
      setBooks((prevBooks) => [...prevBooks, book]);
      setIsbn('');
    }
  };

  const handleCreateReadingList = async () => {
    if (readingListName) {
      try {
        const newId = readingLists.length > 0 ? Math.max(...readingLists.map(list => list.id)) + 1 : 1;
        const response = await fetch(`http://localhost:8080/api/v1/readingList/add-reading-list/${readingListName}`, {
          method: 'POST',
        });

        if (response.ok) {
          let newReadingList;
          const contentType = response.headers.get('content-type');

          if (contentType && contentType.includes('application/json')) {
            newReadingList = await response.json();
          } else {
            await response.text();
            newReadingList = { name: readingListName, id: newId };
          }

          setReadingLists((prevLists) => [...prevLists, newReadingList]);
          setReadingListName('');
        }
      } catch (error) {
        console.error('Error creating reading list:', error);
      }
    }
  };

  // Add a book to a specific reading list
const handleAddBookToReadingList = async (listId) => {
  const bookId = bookIds[listId];
  if (!bookId) {
    alert('Please enter a Book ID');
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/v1/readingList/add-book-to-reading-list/${bookId}/${listId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
    });

    if (response.ok) {
      console.log(`Book ID ${bookId} added to Reading List ID ${listId}`);
      
      fetchReadingListBooks(listId);
      
      setBookIds((prev) => ({ ...prev, [listId]: '' }));
    } else {
      console.error('Failed to add book to the reading list');
    }
  } catch (error) {
    console.error('Error adding book to reading list:', error);
  }
};


  const fetchReadingListBooks = async (listId) => {
  try {
    const response = await fetch(`http://localhost:8080/api/v1/readingList/get-reading-list-books/${listId}`, {
      method: 'GET',
    });

    if (response.ok) {
      const booksInList = await response.json();
      setReadingListBooks((prev) => ({ ...prev, [listId]: booksInList }));
    } else {
      console.error('Failed to fetch books for the reading list');
    }
  } catch (error) {
    console.error('Error fetching books for reading list:', error);
  }
};

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-100 to-indigo-200 flex items-center justify-center p-4">
      <div className="container bg-white rounded-lg shadow-xl p-8 max-w-4xl flex space-x-8">
        
        <div className="w-1/2">
          <h2 className="text-3xl font-bold text-gray-800 mb-6">Books</h2>
          <input
            type="text"
            placeholder="Enter ISBN"
            value={isbn}
            onChange={(e) => setIsbn(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md mb-4"
          />
          <button
            onClick={handleAddBook}
            className="w-full bg-purple-600 text-white py-2 rounded-md mb-6"
          >
            Add Book
          </button>

          {books.length > 0 && (
            <ul className="mt-4 space-y-2">
              {books.map((book, index) => (
                <li key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded">
                  {book.coverImageUrl && (
                    <div className="md:flex-shrink-0">
                      <img
                        src={book.coverImageUrl}
                        alt={book.title}
                        width={200}
                        height={300}
                        className="h-full w-full object-cover md:w-48"
                      />
                    </div>
                  )}
                  <div className="p-8">
                    <h2 className="text-2xl font-bold mb-2">{book.title}</h2>
                    <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mb-2">
                      {book.isbn}
                    </span>
                    <p className="text-sm text-gray-600 mb-4">
                      <span className="font-semibold">Author:</span> {book.author}
                    </p>
                    <p className="text-sm text-gray-600 mb-4">
                      <span className="font-semibold">Number of Pages:</span> {book.numberOfPages}
                    </p>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="w-1/2">
          <h2 className="text-3xl font-bold text-gray-800 mb-6">Reading Lists</h2>
          <input
            type="text"
            placeholder="Enter Reading List Name"
            value={readingListName}
            onChange={(e) => setReadingListName(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md mb-4"
          />
          <button
            onClick={handleCreateReadingList}
            className="w-full bg-green-600 text-white py-2 rounded-md mb-6"
          >
            Create Reading List
          </button>

          <ul className="space-y-4">
            {readingLists.map((list) => (
              <li key={list.id} className="bg-gray-100 p-4 rounded">
                <div className="flex justify-between items-center">
                  <h3 className="text-lg font-semibold text-red-600">Reading list {list.name}</h3>
                </div>
                <div className="mt-4">
                  <input
                    type="text"
                    placeholder="Enter Book ID"
                    value={bookIds[list.id] || ''}
                    onChange={(e) => setBookIds((prev) => ({ ...prev, [list.id]: e.target.value }))}
                    className="w-full px-4 py-2 border border-gray-300 rounded-md mb-2"
                  />
                  <button
                    onClick={() => handleAddBookToReadingList(list.id)}
                    className="w-full bg-blue-600 text-white py-1 rounded-md"
                  >
                    Add Book to List
                  </button>
                </div>

                {readingListBooks[list.id] && (
                  <ul className="mt-4 space-y-2">
                    {readingListBooks[list.id].map((book) => (
                      <li key={book.id} className="text-sm bg-gray-200 p-2 rounded">
                        Book : {book.title} by author : {book.author}
                      </li>
                    ))}
                  </ul>
                )}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
