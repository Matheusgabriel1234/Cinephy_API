Cinephy API

Cinephy é uma API desenvolvida para gerenciar informações sobre filmes, top 10 de usuários e avaliações de filmes. A API integra a OMDb API para buscar informações detalhadas de filmes e fornece operações relacionadas a usuários, avaliações e listas personalizadas.

Funcionalidades Principais

Filmes

Busca por Título:

Endpoint: GET /api/movies/search

Descrição: Busca filmes por título usando a OMDb API.

Detalhes do Filme:

Endpoint: GET /api/movies/details/{imdbId}

Descrição: Obtém detalhes de um filme específico a partir do imdbId.

Top 10 de Filmes por Usuário

Adicionar Filme ao Top 10:

Endpoint: POST /api/movies/top10/{imdbId}

Descrição: Adiciona um filme ao Top 10 de um usuário autenticado.

Remover Filme do Top 10:

Endpoint: DELETE /api/movies/top10/{imdbId}

Descrição: Remove um filme do Top 10 de um usuário autenticado.

Listar Top 10:

Endpoint: GET /api/movies/top10

Descrição: Retorna a lista dos 10 melhores filmes de um usuário autenticado.

Avaliações

Adicionar Avaliação:

Endpoint: POST /api/reviews/{imdbId}

Descrição: Adiciona uma avaliação de um filme com rating e comentário.

Editar Avaliação:

Endpoint: PUT /api/reviews/{id}

Descrição: Edita uma avaliação existente de um filme.

Remover Avaliação:

Endpoint: DELETE /api/reviews/{id}

Descrição: Remove uma avaliação de um filme realizada por um usuário autenticado.

Listar Avaliações de um Filme:

Endpoint: GET /api/movies/details/{imdbId}

Descrição: Retorna todas as avaliações de um filme, incluindo os comentários e notas.

Usuários

Registrar Usuário:

Endpoint: POST /api/auth/register

Descrição: Registra um novo usuário com email, senha, nickname e data de nascimento.

Login:

Endpoint: POST /api/auth/login

Descrição: Realiza autenticação e retorna um token JWT para acesso autenticado.

Obter Dados do Usuário pelo Token:

Descrição: Função interna usada para autenticar e autorizar usuários em operações específicas.

Detalhes de Implementação

Integração com OMDb API:

Usa a OMDb API para buscar dados de filmes.

Endpoints de busca e detalhes são diferenciados.

O mapeamento dos campos do JSON da API é feito nas classes OmdbResponse e MoviesDetailsDTO.

Autenticação com JWT:

Geração de tokens para autenticação.

Validação e extração de informações do token.

Camada de Serviço:

Gerenciamento de usuários, filmes e avaliações usando serviços separados (UserService, MovieService, ReviewService).

Persistência dos dados com JPA e PostgreSQL.

Validações:

As avaliações têm um intervalo válido de 0 a 10.

Cada usuário pode ter no máximo 10 filmes no Top 10.

Avaliações e modificações podem ser feitas apenas pelos autores.

Configuração

Chave OMDb API:

Defina a variável omdb.api.key no application.properties.

Banco de Dados:

Configure o PostgreSQL no application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/cinephy
spring.datasource.username=
spring.datasource.password=

Autenticação JWT:

Defina o segredo JWT no application.properties:

jwt.secret=
jwt.validation=360000

Swagger:

Documentação disponível em /swagger-ui.html.

Dependências

Spring Boot (Web, Data JPA, Security)

PostgreSQL

OMDb API

JWT Authentication

Swagger (springdoc-openapi)

Como Executar

Clone o repositório.

Configure as variáveis no application.properties.

Execute o projeto com o comando: mvn spring-boot:run.

Acesse a API em http://localhost:8080 (ou outra porta configurada).

Endpoints Principais

Autenticação

POST /api/auth/register: Registra novo usuário.

POST /api/auth/login: Retorna token JWT.

Filmes

GET /api/movies/search: Busca por título.

GET /api/movies/details/{imdbId}: Detalhes do filme.

POST /api/movies/top10/{imdbId}: Adiciona ao Top 10.

DELETE /api/movies/top10/{imdbId}: Remove do Top 10.

Avaliações

POST /api/reviews/{imdbId}: Adiciona avaliação.

PUT /api/reviews/{id}: Edita avaliação.

DELETE /api/reviews/{id}: Remove avaliação.
