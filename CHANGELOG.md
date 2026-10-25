# Changelog

Formato baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
versionamento [SemVer](https://semver.org/lang/pt-BR/).

## [Unreleased]

## [0.3.0] - 2026-10-24

### Added
- Modelo de domínio documentado (diagrama ER e máquina de estados da corrida).
- Migrations Flyway: usuários, motoristas, veículos, corridas, avaliações e pagamentos.
- Entidades JPA, enums e repositories com consultas paginadas e sem N+1.
- Testes de persistência com Testcontainers (PostgreSQL real), cobrindo constraints e índices.
- ADR 0003: modelagem do domínio.

### Changed
- CI sem serviço de banco: os testes sobem o próprio PostgreSQL.

## [0.2.0] - 2026-10-24

### Added
- Projeto Spring Boot 4.1 (Java 21, Gradle Kotlin DSL) em `backend/`.
- PostgreSQL 17 via Docker Compose e perfil `dev`.
- Spring Data JPA e Flyway com migration baseline.
- Tratamento global de erros com ProblemDetail (RFC 9457) e testes.
- Actuator restrito a `health` e `info`.
- CI com GitHub Actions (build e testes do backend).
- ADR 0002: Spring Boot 4.1 e Java 21.

## [0.1.0] - 2026-10-22

### Added
- Estrutura do monorepo (`backend/`, `android/`, `docs/`).
- Arquivos-base: `.gitignore`, `.gitattributes`, `.editorconfig`, `LICENSE`.
- Guia de contribuição, templates de PR e issue.
- ADR 0001: monorepo e Gradle.



