# Contribuindo

Este projeto segue um fluxo próximo ao de times reais, mesmo sendo desenvolvido individualmente.

## Fluxo

1. Toda mudança parte de uma **issue** ligada a um **milestone** (uma fase do roadmap).
2. Cria-se uma branch a partir da `main`: `<tipo>/phase-NN-<slug>`
   (ex.: `feat/phase-04-jwt-auth`).
3. Commits pequenos, no padrão **Conventional Commits**.
4. Abre-se um **Pull Request** para `main`, preenchendo o template.
5. Merge por **rebase**, mantendo o histórico linear e os commits de cada etapa.
6. Ao fechar uma fase, cria-se uma **tag** `vMAJOR.MINOR.PATCH` e uma release no GitHub.

A `main` é protegida: não há push direto.

## Conventional Commits

Formato: `tipo(escopo): descrição no imperativo, em inglês, até ~72 caracteres`

| Tipo | Uso |
|---|---|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `docs` | Somente documentação |
| `refactor` | Mudança sem alterar comportamento |
| `test` | Testes |
| `build` | Build, dependências |
| `ci` | Pipelines |
| `chore` | Manutenção, configuração |
| `perf` | Desempenho |

Escopos usados: `backend`, `android`, `infra`, `docs`, `ci`, `github`, `deps`.

Exemplos:

- `feat(backend): add refresh token rotation`
- `fix(android): handle null location on first fix`
- `chore(infra): add postgres to docker-compose`

## Versionamento

[SemVer](https://semver.org/lang/pt-BR/). Cada fase concluída gera `v0.N.0`;
o produto completo é a `v1.0.0`.

## Segredos

Nunca commitar `.env`, chaves, tokens ou `local.properties`.
Use `.env.example` com valores fictícios.
