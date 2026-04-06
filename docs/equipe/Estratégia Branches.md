# 🌿 Estratégia de Branching: GitHub Flow 

Esta organização utiliza o GitHub Flow como fluxo de trabalho padrão. O foco é manter a branch principal sempre estável e utilizar Pull Requests para todas as alterações.

## 🟢 Branch Principal (main)

A branch main contém o código que está em produção e estável. Esta branch é protegida e exige revisão antes de qualquer merge.
* Nunca faça commits diretamente na main.
* Toda alteração deve vir de uma branch secundária (develop).

## 🛠️ Branches de Desenvolvimento (develop)

Para cada sprint deve ser criada uma nova branch de desenvolvimento a partir da main. <br>
Enquanto a branch **main** contém exclusivamente o código em estado de produção (estável), a **develop** concentra as versões mais recentes das tarefas concluídas pela equipe.


## 🔄 Commits e Sincronização

Faça commits frequentes com mensagens claras e objetivas. <br>
Mantenha seu repositório local atualizado para evitar conflitos no final do desenvolvimento.

## ⚖️ Pull Requests (PR)

Assim que tiver iniciado o trabalho (mesmo que não esteja pronto), abra um Pull Request.
* Use o modo "Draft" se o código ainda estiver em desenvolvimento.
* O PR é o lugar para revisões de código, sugestões e testes antes do merge.

## ✅ Merge e Deploy

Uma vez que o PR foi aprovado por pelo menos um revisor e passou nos testes, ele pode ser mesclado à main. Após o merge, a branch de desenvolvimento deve ser excluída para manter o repositório limpo.
