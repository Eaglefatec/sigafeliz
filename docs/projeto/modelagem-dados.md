# Modelagem de Dados: Siga Feliz

> **Fonte de verdade:** `backlog-geral.md`
> O Excel gerado (US09) **não é salvo no banco** — é gerado na hora e descartado.

---

## Tabelas

### `professor`
Cadastro dos docentes, feito pela coordenação (US02).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(255) | **Chave primária** |
| `email` | VARCHAR(255) | Único, não pode repetir |

> 💡 Usamos `nome` como chave pois o professor entra por nome. Se dois professores tiverem o mesmo nome, a equipe decide se usa email como PK em vez disso.

---

### `semestre`
Parâmetros do período letivo, configurados pela coordenação (US01).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(20) | **Chave primária** (ex: `"2025.1"`) |
| `data_inicio` | DATE | Data de início das aulas |
| `data_fim` | DATE | Data de encerramento das aulas |
| `data_kickoff` | DATE | Marco zero do Projeto Integrador (US04) |

> 💡 Os **ciclos de Sprint** (28 dias a partir do kickoff) são **calculados pelo sistema**, não ficam salvos como linhas na tabela.

---

### `sabado_letivo`
Sábados disponíveis para compensação de carga horária (US01, US07).

| Coluna | Tipo | Observação |
|---|---|---|
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 1)** — FK → `semestre.nome` |
| `data` | DATE | **Chave primária (parte 2)** |

> 💡 A chave primária é composta pelas duas colunas juntas, pois um sábado pertence a um semestre específico.

---

### `dia_restrito`
Feriados e eventos sem aula, cadastrados pela coordenação (US01).

| Coluna | Tipo | Observação |
|---|---|---|
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 1)** — FK → `semestre.nome` |
| `data` | DATE | **Chave primária (parte 2)** |
| `descricao` | VARCHAR(255) | Opcional (ex: `"Feriado Nacional"`) |

---

### `disciplina`
Grade de uma matéria vinculada a um professor num semestre (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(255) | **Chave primária (parte 1)** (ex: `"Banco de Dados 1"`) |
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 2)** — FK → `semestre.nome` |
| `professor_nome` | VARCHAR(255) | FK → `professor.nome` |
| `carga_horaria_total` | INT | Deve ser `40` ou `80` |

> 💡 Uma disciplina é identificada pelo nome + semestre (ex: "BD1" pode existir no 2025.1 e no 2025.2).

---

### `aulas_por_dia`
Quantas aulas a disciplina tem em cada dia da semana (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina.nome` |
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 2)** — FK → `disciplina.semestre_nome` |
| `dia_semana` | INT | **Chave primária (parte 3)** — `1`=Seg, `2`=Ter, ..., `6`=Sáb |
| `quantidade_aulas` | INT | Ex: `2` (duas aulas por dia nesse dia da semana) |

---

### `tema`
Conteúdo programático de cada disciplina, inserido pelo professor (US06, US08).

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina.nome` |
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 2)** — FK → `disciplina.semestre_nome` |
| `titulo` | VARCHAR(255) | **Chave primária (parte 3)** |
| `carga_minima` | INT | Mínimo de aulas para este tema |
| `carga_maxima` | INT | Máximo de aulas para este tema |
| `prioridade` | VARCHAR(5) | `'ALTA'`, `'MEDIA'` ou `'BAIXA'` |
| `e_avaliacao` | BOOLEAN | `TRUE` se for uma prova (US08) |
| `ordem` | INT | Posição do tema na ementa (1º, 2º...) |

---

## Relacionamentos

```
semestre  ──< sabado_letivo   (1 semestre tem vários sábados)
semestre  ──< dia_restrito    (1 semestre tem vários feriados/eventos)
semestre  ──< disciplina      (1 semestre tem várias disciplinas)
professor ──< disciplina      (1 professor ministra várias disciplinas)
disciplina ──< aulas_por_dia  (1 disciplina tem grade em vários dias)
disciplina ──< tema           (1 disciplina tem vários temas)
```

---

## O que NÃO fica no banco

| Item | Por quê |
|---|---|
| Ciclos de Sprint (28 dias) | Calculado a partir de `semestre.data_kickoff` |
| Saldo de dias úteis | Calculado cruzando grade, semestre e feriados |
| Arquivo Excel (.xlsx) | Gerado na hora (US09), não precisa ser salvo |
