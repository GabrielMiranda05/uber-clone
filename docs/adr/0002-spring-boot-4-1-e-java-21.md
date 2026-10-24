# ADR 0002: Spring Boot 4.1 e Java 21

- **Status:** aceito
- **Data:** 2026-10-24

## Contexto

O roteiro original previa Spring Boot 3. O Spring Boot 3.5 teve o suporte
open source encerrado em 30/06/2026. As linhas vigentes são 4.0 (até 31/12/2026)
e 4.1 (até 31/07/2027).

## Decisão

- **Spring Boot 4.1** sobre **Java 21 (LTS)**.
- Dependências adicionadas de forma incremental, na etapa que as utiliza.
- Pacotes organizados **por feature**, com camadas dentro de cada uma.
- **Lombok** com uso restrito: sem `@Data`/`@EqualsAndHashCode` em entidades JPA;
  DTOs como `record`.

## Consequências

- (+) Versão com suporte e correções de segurança.
- (-) Menos material de comunidade que o Boot 3; bibliotecas de terceiros
  precisam de versões compatíveis com o Boot 4 (verificar a cada adição).
