# Modelagem de Dados: Siga Feliz

> **Fonte de verdade:** `backlog-geral.md`
> 
---

## Tabelas

### `professor`
Cadastro dos docentes, feito pela coordenação (US02).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(255) | N/A |
| `email` | VARCHAR(255) | **Chave primária** |

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
Catálogo da matéria. Cadastrada uma única vez pela coordenação (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(255) | **Chave primária** (ex: `"Banco de Dados 1"`) |
| `carga_horaria_total` | INT | Deve ser `40` ou `80` |

---

### `tema`
Conteúdo programático fixo da disciplina, inserido pelo professor (US06, US08). Pertence ao catálogo e é reaproveitado entre semestres.

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina.nome` |
| `titulo` | VARCHAR(255) | **Chave primária (parte 2)** |
| `carga_minima` | INT | Mínimo de aulas para este tema |
| `carga_maxima` | INT | Máximo de aulas para este tema |
| `prioridade` | VARCHAR(5) | `'ALTA'`, `'MEDIA'` ou `'BAIXA'` |
| `e_avaliacao` | BOOLEAN | `TRUE` se for uma prova (US08) |
| `ordem` | INT | Posição do tema na ementa (1º, 2º...) |

---

### `disciplina_ofertada`
Vínculo volátil que ativa uma disciplina em um semestre específico e a atribui a um docente (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina.nome` |
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 2)** — FK → `semestre.nome` |
| `professor_email` | VARCHAR(255) | FK → `professor.email` |

---

### `aulas_por_dia`
A grade de dias da semana específica daquela oferta no semestre (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina_ofertada.disciplina_nome` |
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 2)** — FK → `disciplina_ofertada.semestre_nome` |
| `dia_semana` | INT | **Chave primária (parte 3)** — `1`=Seg, `2`=Ter, ..., `6`=Sáb |
| `quantidade_aulas` | INT | Ex: `2` (duas aulas por dia nesse dia da semana) |

---

## Relacionamentos

```
semestre            ──< sabado_letivo        (1 semestre tem vários sábados)
semestre            ──< dia_restrito         (1 semestre tem vários feriados/eventos)
semestre            ──< disciplina_ofertada  (1 semestre possui várias disciplinas ofertadas)
professor           ──< disciplina_ofertada  (1 professor ministra várias disciplinas ofertadas)
disciplina          ──< disciplina_ofertada  (1 disciplina pode ser ofertada em vários semestres)
disciplina          ──< tema                 (1 disciplina tem vários temas fixos)
disciplina_ofertada ──< aulas_por_dia        (1 oferta possui grade em vários dias da semana)
```

---

## O que NÃO fica no banco

| Item | Por quê |
|---|---|
| Ciclos de Sprint (28 dias) | Calculado a partir de `semestre.data_kickoff` |
| Saldo de dias úteis | Calculado cruzando grade, semestre e feriados |
| Arquivo Excel (.xlsx) | Gerado na hora (US09), não precisa ser salvo |
