# Plugin: Better Wind Charge

Esse projeto é um plugin para Minecraft desenvolvido como 
resposta ao desafio de recrutamento da Enx, com o objetivo 
trazer mais funcionalidades ao item Wind Charge do jogo.
É um plugin simples que traz um comando com múltiplas 
opções para manipulação do Wind Charge.

## Índice

- [Tecnologias](#tecnologias)
- [Executando o projeto](#executando-o-projeto)
- [Classe principal](#classe-principal)
- [Comando](#comando)
- [Evento](#evento)

## Tecnologias

Para o desenvolvimento do plugin foi utilizado:
- Java 21
- Gradle 8.9
- Spigot 1.21
- Docker
- Redis 6.2
- Git

## Executando o projeto

Para executar o projeto deve ser simples, já que o arquivo
do Docker `Dockerfile` cuida de todo o processo de _build_,
cópia do arquivo `.jar` e abertura do servidor Spigot.

Bastar possuir Docker instalado em sua máquina e usar
no terminal:
```
docker compose up --build
```
Obs.: talvez seja necessário usar `sudo` no início do
comando caso o sistema operacional seja Linux e o docker
não possua privilégios para rodar sem a permissão 
do administrador.

O comando irá criar os conteineres necessários e irá
executar o `.jar` do Spigot, abrindo o servidor.
Ao finalizar, deve-se esperar uma mensagem similar à
abaixo:
```
[Server thread/INFO]: [pluginenxtest] Enabling pluginenxtest v1.0-SNAPSHOT
```
Significando que o plugin foi ativado no servidor.
A mensagem não deve ser seguida de nenhum tipo de erro.

Finalmente, basta abrir o cliente do Minecraft em sua
versão 1.21, escolher a opção "Multiplayer", criar um
servidor com o endereço `0` ou `localhost` e entrar!

## Classe principal

> `com.ethamorim.pluginenxtest.EnxTestPlugin`

Essa classe possui os métodos de ciclo de vida do
plugin, `onEnable` e `onDisable` onde tarefas são
executadas quando o plugin é ativado e desativado,
respectivamente.

O método `onEnable` define as configurações na instância
do Redis e registra o comando e os eventos, além da
tarefa que assiste os projéteis lançados e adiciona
partículas no seu rastro.

Essa tarefa utiliza do `BukkitScheduler` para registrar
uma tarefa que se repete a cada 1 tick. Ela utiliza dos
projéteis adicionados ao mapa de projéteis para adicionar
partículas de fogos de artifícios em sua localização, 
que muda a cada tick. Tendo partículas adicionadas 
a cada atualização de posição, o projétil voa com o 
efeito de rastro atrás dele. Essa adição, no entanto,
só é feita caso a configuração `trailing-particles` esteja
ativa.

No método `onDisable`, todas as tarefas adicionadas ao
`BukkitScheduler` são removidas.

## Comando

> `com.ethamorim.pluginenxtest.command.WindChargeCommand`

A funcionalidade principal do servidor se deve ao novo
comando capaz de alterar certas propriedades do
item Wind Charge `/windcharge` (ou seu alias `/wc`).

Com ele, basta utilizar dos seus argumentos para executar
diferentes modificações.

### - `give <amount>`

Presenteia o player com a quantidade especificada 
de Wind Charges.

Ex.:
```
/windcharge give 64
```
Irá presentear o jogador com um stack de 64 Wind Charges.

### - `set <propertie> <option>`

Modifica a funcionalidade especificada do Wind Charge.
Existem três opções para `<propertie>`, enquanto o
último argumento `<option>` é dependente totalmente
de `<propertie>`.

- `velocity`: modifica a velocidade do projétil
  - `static`: o projétil não terá velocidade nem 
  aceleração ao ser lançado, se mantendo no local 
  em que o jogador o atirou
  - `slow`: o projétil tem sua velocidade reduzida
  em 95%.
  - `default`: o projétil volta a sua velocidade 
  original
  - `fast`: o projétil tem sua velocidade dobrada
  - `lightning`: a velocidade é multiplicada por 8
  

- `power`: modifica a potência de explosão do Wind Charge
  - `default`: é a potência original da explosão (0.0)
  - `medium`: potência mediana (0.05)
  - `high`: alta potência (0.2)
  - `huge`: potência enorme (1.0)
  

- `trailing_particles`: modifica a propriedade de 
partículas de rastro, fazendo partículas de fogos 
de artifício seguirem o projétil no seu trajeto
  - `false`: é o valor padrão
  - `true`: ativa a configuração

Todas as configurações são inicializadas na instância do
Redis aberta pelo Docker, e modificadas nela. Sendo assim,
ao usar o comando...

```
/wc set power high
```

...a propriedade de fator de explosão no Redis será modificada
para o valor correspondente a `high`.

## Evento

> `com.ethamorim.pluginenxtest.WindChargeEvent`

O comando e suas opções trabalham junto com os eventos,
estes que, por sua vez, mudam o comportamento do Wind
Charge de fato.

Visto que o projétil Wind Charge é a entidade que possui
as propriedades desejadas de mudança, foram utilizados
eventos para manipular o comportamento desse projétil.

### onProjectileLaunch

Esse evento é lançado quando um projétil é atirado,
e esse método modifica diretamente a velocidade do
projétil.

O valor na configuração `velocity-factor`, modificado
pelo argumento `set velocity` do comando, é utilizado
como um fator no resultado da velocidade, que no
projétil se trata de um vetor. Cada valor do vetor
é multiplicada com o valor da configuração, resultando
em uma velocidade maior ou menor para o projétil do 
Wind Charge.

Esse método também adiciona o projétil ao mapa de 
projéteis, que por sua vez, caso `trailing-particles`
esteja ativo, passa a adicionar partículas no rastro
do projétil.

### onProjectileHit

Esse evento é lançado quando o projétil pousa.
Esse método faz três coisas: remove o projétil
do mapa de projéteis, fazendo com que a tarefa 
no `BukkitScheduler` do plugin pare de seguir 
o projétil pousado (já que passará a não existir 
mais), cria uma explosão com a potência determinada 
na configuração `explosion-factor`, e _spawna_ 
partículas correspondentes à explosão, com a 
potência maior recebendo o efeito de maior explosão.

A explosão é criada pois não foi possível modificar a
explosão _built-in_ do projétil, visto que alteração
nas propriedades relacionadas à interface `Explosive`
não faziam diferença. Sendo assim, é utilizado da localização
de pouso do projétil no momento em que ele atinge algo
para criar uma explosão no mundo. A potência dessa
explosão é exatamente igual ao valor da configuração
`explosion-factor`.

O valor não foi utilizado como fator de resultado, pois
a potência da explosão criada não se comportada da mesma
forma que o vetor de velocidade do projétil. A menor
potência se encontra no 0.0, enquanto incremento nesse
valor traz alterações significativas no comportamento
da explosão.

### onEntityDamage

Esse evento é lançado quando uma entidade é ferida.
O método, por sua vez, é simples. Apenas verifica
se a entidade ferida é o jogador, e se a causa é uma
explosão, se verdadeiro, então o dano é anulado.

É sabido que essa condição anula dano de qualquer
outro tipo de explosão, porém decidiu-se mantê-lo
mesmo assim visto que as explosões maiores criadas
pelo evento `onProjectileHit` podem matar o jogador
de primeira.
