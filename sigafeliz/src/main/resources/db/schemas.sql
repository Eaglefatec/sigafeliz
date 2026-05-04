-- public.aula_por_dia definition

-- Drop table

-- DROP TABLE public.aula_por_dia;

CREATE TABLE public.aula_por_dia (
                                     disciplina_nome varchar(255) NULL,
                                     dia_semana int4 NULL,
                                     quantidade_aulas int4 NULL,
                                     CONSTRAINT aula_por_dia_unique UNIQUE (disciplina_nome, dia_semana)
);


-- public.professor definition

-- Drop table

-- DROP TABLE public.professor;

CREATE TABLE public.professor (
                                  nome varchar(255) NULL,
                                  email varchar(255) NOT NULL,
                                  CONSTRAINT professor_unique UNIQUE (email)
);


-- public.semana definition

-- Drop table

-- DROP TABLE public.semana;

CREATE TABLE public.semana (
                               semana varchar(7) NOT NULL,
                               CONSTRAINT semana_pk PRIMARY KEY (semana)
);


-- public.semestre definition

-- Drop table

-- DROP TABLE public.semestre;

CREATE TABLE public.semestre (
                                 nome varchar(20) NOT NULL,
                                 data_inicio date NULL,
                                 data_fim date NULL,
                                 data_kickoff date NULL,
                                 CONSTRAINT semestre_unique UNIQUE (nome)
);


-- public.dia_restrito definition

-- Drop table

-- DROP TABLE public.dia_restrito;

CREATE TABLE public.dia_restrito (
                                     semestre_nome varchar(20) NOT NULL,
                                     "data" date NOT NULL,
                                     descricao varchar(50) NULL,
                                     CONSTRAINT dia_restrito_pk PRIMARY KEY (semestre_nome, data),
                                     CONSTRAINT dia_restrito_semestre_fk FOREIGN KEY (semestre_nome) REFERENCES public.semestre(nome) ON DELETE CASCADE ON UPDATE CASCADE
);


-- public.disciplina definition

-- Drop table

-- DROP TABLE public.disciplina;

CREATE TABLE public.disciplina (
                                   nome varchar(255) NOT NULL,
                                   professor_email varchar(255) NULL,
                                   carga_horaria_total int4 NULL,
                                   aula_segunda int2 DEFAULT 0 NOT NULL,
                                   aula_terca int2 DEFAULT 0 NOT NULL,
                                   aula_quarta int2 DEFAULT 0 NOT NULL,
                                   aula_quinta int2 DEFAULT 0 NOT NULL,
                                   aula_sexta int2 DEFAULT 0 NOT NULL,
                                   CONSTRAINT disciplina_pk PRIMARY KEY (nome),
                                   CONSTRAINT disciplina_professor_fk FOREIGN KEY (professor_email) REFERENCES public.professor(email)
);


-- public.sabado_letivo definition

-- Drop table

-- DROP TABLE public.sabado_letivo;

CREATE TABLE public.sabado_letivo (
                                      "data" date NULL,
                                      semestre_nome varchar NULL,
                                      CONSTRAINT sabado_letivo_semestre_fk FOREIGN KEY (semestre_nome) REFERENCES public.semestre(nome) ON DELETE CASCADE ON UPDATE CASCADE
);


-- public.tema definition

-- Drop table

-- DROP TABLE public.tema;

CREATE TABLE public.tema (
                             disciplina_nome varchar(100) NOT NULL,
                             titulo varchar(255) NOT NULL,
                             carga_minima int4 NULL,
                             carga_maxima int4 NULL,
                             prioridade varchar(5) NULL,
                             e_avaliacao bool NULL,
                             ordem int4 NOT NULL,
                             CONSTRAINT tema_pk PRIMARY KEY (disciplina_nome, titulo),
                             CONSTRAINT tema_unique UNIQUE (disciplina_nome, ordem),
                             CONSTRAINT tema_disciplina_fk FOREIGN KEY (disciplina_nome) REFERENCES public.disciplina(nome)
);