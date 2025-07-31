# LiterAlura - Challenge Java

<p align="center">
     <a alt="Java" href="https://java.com" target="_blank">
        <img src="https://img.shields.io/badge/Java-v22.0.1-ED8B00.svg" />
    </a>
    <a alt="Spring Framework" href="https://spring.io/" target="_blank">
        <img src="https://img.shields.io/badge/Spring-v3.3.0-6DB33F.svg" />
    </a>
     <a alt="Maven" href="https://maven.apache.org/index.html" target="_blank">
        <img src="https://img.shields.io/badge/Maven-v4.0.0-CD2335.svg" />
    </a>
    <a alt="Jackson" href="https://github.com/FasterXML/jackson" target="_blank">
        <img src="https://img.shields.io/badge/Jackson-v2.17.0-36AAFD.svg" />
    </a>
    <a alt="PostgreSQL" href="https://postgresql.org" target="_blank">
        <img src="https://img.shields.io/badge/PostgreSQL-v.15.6-316192.svg" />
    </a>
</p>

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
4. 