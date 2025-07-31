# LiterAlura - Challenge Java

Catálogo de livros em Java com interação via console, consumindo dados de uma API pública, armazenando-os em banco de dados e permitindo consultas e filtros.

## Opções do Programa

--- LITERALURA ---
1. - Buscar livro por título
2. - Listar livros registrados
3. - Listar autores registrados
4. - Listar autores vivos em um ano
5. - Listar livros por idioma
0. - Sair

## Funcionalidades

- Busca livros pelo título via API;
- Registro automático de livros e autores no banco de dados;
- Listagem de livros e autores cadastrados;
- Filtros por ano de vida do autor e idioma do livro;
- Interface textual simples e intuitiva.

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
