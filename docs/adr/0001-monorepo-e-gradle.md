# ADR 0001: Monorepo e Gradle (Kotlin DSL)

- **Status:** aceito
- **Data:** 2026-10-22

## Contexto

O projeto tem um back-end (Java/Spring) e um app Android que evoluem juntos:
uma feature (ex.: acompanhamento em tempo real) toca os dois lados.

## Decisão

1. **Monorepo**, com `/backend` e `/android` no mesmo repositório.
2. **Gradle com Kotlin DSL** nos dois módulos.

## Consequências

- (+) Um PR/issue conta a feature inteira; um único link para avaliação.
- (+) Mesma ferramenta de build nos dois módulos reduz o custo cognitivo.
- (-) CI precisa filtrar por pasta para não rodar tudo a cada mudança.
- (-) Maven é mais comum em back-end Java corporativo; a escolha prioriza
  consistência com o Android.
