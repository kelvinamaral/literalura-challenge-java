# LiterAlura - Challenge Java

Catálogo de livros em Java com interação via console, consumindo dados de uma API pública, armazenando-os em banco de dados e permitindo consultas e filtros.

## Funcionalidades

- 🔍 Buscar livro por título através de API externa
- 💾 Registrar livros e autores automaticamente no banco de dados
- 📚 Listar livros cadastrados
- 🧑‍💼 Listar autores cadastrados
- 📅 Filtrar autores vivos em um ano específico
- 🌐 Listar livros por idioma
- 🖥️ Interface via console simples e amigável

## Tecnologias

- Java
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- API REST externa para dados de livros

## Como usar

1. Configure o banco PostgreSQL e atualize as propriedades no `application.properties`.
2. Rode a aplicação (`mvn spring-boot:run` ou via IDE).
3. Interaja via menu no console seguindo as instruções.
