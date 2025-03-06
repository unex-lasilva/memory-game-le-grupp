fun main() {
    import kotlin.random.Random

// Enumeração para representar as cores das cartas
            enum class CorCarta {
        AZUL, VERMELHO, AMARELO, PRETO
    }

    // Classe para representar uma carta
    class Carta(val id: Int, val cor: CorCarta, var virada: Boolean = false, var encontrada: Boolean = false) {
        override fun toString(): String {
            return if (virada || encontrada) {
                when (cor) {
                    CorCarta.AZUL -> "🔵$id"
                    CorCarta.VERMELHO -> "🔴$id"
                    CorCarta.AMARELO -> "🟡$id"
                    CorCarta.PRETO -> "⚫$id"
                }
            } else {
                "🎴" // Carta virada para baixo
            }
        }
    }

    // Classe para representar um participante
    class Participante(val nome: String, val cor: CorCarta, var pontuacao: Int = 0) {
        fun ganharPontos(pontos: Int) {
            pontuacao += pontos
        }

        fun perderPontos(pontos: Int) {
            pontuacao = maxOf(0, pontuacao - pontos)
        }
    }

    // Classe principal do jogo
    class JogoDaMemoria {
        private var tabuleiro = Array(4) { Array<Carta?>(4) { null } }
        private var participantes = arrayOf<Participante>()
        private var participanteAtual = 0
        private var tamanhoTabuleiro = 4
        private var cartaViradaAtualmente: Carta? = null
        private var posicaoCartaVirada: Pair<Int, Int>? = null

        // Inicializa o jogo
        fun iniciar() {
            println("\n==== JOGO DA MEMÓRIA ====")

            var opcao: Int
            do {
                exibirMenu()
                opcao = lerOpcao()

                when (opcao) {
                    1 -> {
                        configurarJogo()
                        jogar()
                    }
                    2 -> exibirPontuacoes()
                    3 -> exibirRegras()
                    4 -> println("Obrigado por jogar! Até a próxima!")
                    else -> println("Opção inválida. Tente novamente.")
                }
            } while (opcao != 4)
        }

        // Exibe o menu principal
        private fun exibirMenu() {
            println("\nMENU PRINCIPAL:")
            println("1. Iniciar")
            println("2. Pontuações dos Participantes")
            println("3. Regras do Jogo")
            println("4. Sair")
            print("Escolha uma opção: ")
        }

        // Lê a opção do usuário
        private fun lerOpcao(): Int {
            return try {
                readLine()?.toInt() ?: 0
            } catch (e: NumberFormatException) {
                0
            }
        }

        // Configura o jogo (tamanho do tabuleiro, participantes)
        private fun configurarJogo() {
            solicitarTamanhoTabuleiro()
            criarParticipantes()
            criarTabuleiro()
        }

        // Solicita o tamanho do tabuleiro
        private fun solicitarTamanhoTabuleiro() {
            var tamanhoValido = false
            var tentativas = 0

            while (!tamanhoValido && tentativas < 3) {
                println("\nEscolha o tamanho do tabuleiro:")
                println("1. 4x4")
                println("2. 6x6")
                println("3. 8x8")
                println("4. 10x10")
                print("Opção: ")

                when (readLine()?.toIntOrNull()) {
                    1 -> {
                        tamanhoTabuleiro = 4
                        tamanhoValido = true
                    }
                    2 -> {
                        tamanhoTabuleiro = 6
                        tamanhoValido = true
                    }
                    3 -> {
                        tamanhoTabuleiro = 8
                        tamanhoValido = true
                    }
                    4 -> {
                        tamanhoTabuleiro = 10
                        tamanhoValido = true
                    }
                    else -> {
                        println("Por favor, escolha umas das opções de tamanho de tabuleiro disponíveis")
                        tentativas++
                    }
                }
            }

            if (!tamanhoValido) {
                println("Você errou 3 vezes. O tamanho padrão 4x4 será utilizado.")
                tamanhoTabuleiro = 4
            }

            // Opção que irá aparecer caso escolha o Tabuleiro 4x4
            tamanhoTabuleiro = 4
            println("Tabuleiro 4x4 configurado.")
        }

        // Cria os participantes
        private fun criarParticipantes() {
            println("\nRegistro dos participantes:")

            print("Nome do Participante 1 (cor AZUL): ")
            val nome1 = readLine()?.takeIf { it.isNotBlank() } ?: "PARTICIPANTE01"

            print("Nome do Participante 2 (cor VERMELHA): ")
            val nome2 = readLine()?.takeIf { it.isNotBlank() } ?: "PARTICIPANTE02"

            participantes = arrayOf(
                Participante(nome1, CorCarta.AZUL),
                Participante(nome2, CorCarta.VERMELHO)
            )

            println("\nParticipantes registrados:")
            println("- ${participantes[0].nome} (${participantes[0].cor})")
            println("- ${participantes[1].nome} (${participantes[1].cor})")
        }

        // Cria o tabuleiro com as cartas
        private fun criarTabuleiro() {
            // Para o tabuleiro 4x4 específico
            tabuleiro = Array(tamanhoTabuleiro) { Array(tamanhoTabuleiro) { null } }

            // Criando as cartas de acordo com a especificação
            val cartas = mutableListOf<Carta>()

            // 2 pares de cartas azuis (id 1 e 2)
            cartas.add(Carta(1, CorCarta.AZUL))
            cartas.add(Carta(1, CorCarta.AZUL))
            cartas.add(Carta(2, CorCarta.AZUL))
            cartas.add(Carta(2, CorCarta.AZUL))

            // 2 pares de cartas vermelhas (id 3 e 4)
            cartas.add(Carta(3, CorCarta.VERMELHO))
            cartas.add(Carta(3, CorCarta.VERMELHO))
            cartas.add(Carta(4, CorCarta.VERMELHO))
            cartas.add(Carta(4, CorCarta.VERMELHO))

            // 3 pares de cartas amarelas (id 5, 6 e 7)
            cartas.add(Carta(5, CorCarta.AMARELO))
            cartas.add(Carta(5, CorCarta.AMARELO))
            cartas.add(Carta(6, CorCarta.AMARELO))
            cartas.add(Carta(6, CorCarta.AMARELO))
            cartas.add(Carta(7, CorCarta.AMARELO))
            cartas.add(Carta(7, CorCarta.AMARELO))

            // 1 par de cartas pretas (id 8)
            cartas.add(Carta(8, CorCarta.PRETO))
            cartas.add(Carta(8, CorCarta.PRETO))

            // Embaralhar as cartas
            cartas.shuffle()

            // Distribuir as cartas no tabuleiro
            var index = 0
            for (i in 0 until tamanhoTabuleiro) {
                for (j in 0 until tamanhoTabuleiro) {
                    tabuleiro[i][j] = cartas[index]
                    index++
                }
            }
        }

        // Exibe o tabuleiro atual
        private fun exibirTabuleiro() {
            println("\n  " + (0 until tamanhoTabuleiro).joinToString(" ") { "$it " })

            for (i in 0 until tamanhoTabuleiro) {
                print("$i ")
                for (j in 0 until tamanhoTabuleiro) {
                    print("${tabuleiro[i][j]} ")
                }
                println()
            }
        }

        // Loop principal do jogo
        private fun jogar() {
            var jogoAcabou = false

            while (!jogoAcabou) {
                exibirTabuleiro()
                println("\nVez de ${participantes[participanteAtual].nome} (${participantes[participanteAtual].cor})")
                println("Pontuação: ${participantes[participanteAtual].pontuacao}")

                // Primeira carta
                val primeiraCarta = selecionarCarta() ?: continue
                exibirTabuleiro()

                // Segunda carta
                println("Selecione a segunda carta:")
                val segundaCarta = selecionarCarta() ?: continue
                exibirTabuleiro()

                // Verificar se as cartas formam um par
                verificarPar(primeiraCarta, segundaCarta)

                // Verificar se o jogo acabou
                jogoAcabou = verificarFimDeJogo()

                // Alternar jogador
                participanteAtual = 1 - participanteAtual
            }

            // Anunciar vencedor
            anunciarVencedor()
        }

        // Solicita ao jogador para selecionar uma carta
        private fun selecionarCarta(): Pair<Int, Int>? {
            var tentativas = 0

            while (tentativas < 3) {
                print("Informe a linha (0-${tamanhoTabuleiro-1}): ")
                val linha = readLine()?.toIntOrNull()

                print("Informe a coluna (0-${tamanhoTabuleiro-1}): ")
                val coluna = readLine()?.toIntOrNull()

                if (linha == null || coluna == null || linha < 0 || linha >= tamanhoTabuleiro || coluna < 0 || coluna >= tamanhoTabuleiro) {
                    println("Posição da carta inválida, por favor, insira uma posição válida")
                    tentativas++
                    continue
                }

                // Verificar se a carta já está virada
                if (tabuleiro[linha][coluna]?.virada == true || tabuleiro[linha][coluna]?.encontrada == true) {
                    println("A carta da posição informada já está virada, por favor, escolha outra posição")
                    tentativas++
                    continue
                }

                // Virar a carta
                tabuleiro[linha][coluna]?.virada = true

                if (cartaViradaAtualmente == null) {
                    cartaViradaAtualmente = tabuleiro[linha][coluna]
                    posicaoCartaVirada = Pair(linha, coluna)
                }

                return Pair(linha, coluna)
            }

            println("Você errou 3 vezes. Perdeu a vez.")
            return null
        }

        // Verifica se as cartas formam um par e atualiza a pontuação
        private fun verificarPar(primeiraPosicao: Pair<Int, Int>, segundaPosicao: Pair<Int, Int>) {
            val primeiraCarta = tabuleiro[primeiraPosicao.first][primeiraPosicao.second]
            val segundaCarta = tabuleiro[segundaPosicao.first][segundaPosicao.second]

            if (primeiraCarta?.id == segundaCarta?.id) {
                // Par encontrado
                println("Par encontrado!")
                primeiraCarta?.encontrada = true
                segundaCarta?.encontrada = true

                // Atualizar pontuação
                when (primeiraCarta?.cor) {
                    CorCarta.AMARELO -> {
                        participantes[participanteAtual].ganharPontos(1)
                        println("${participantes[participanteAtual].nome} ganhou 1 ponto!")
                    }
                    CorCarta.PRETO -> {
                        println("${participantes[participanteAtual].nome} encontrou o par preto e ganhou o jogo!")
                        anunciarVencedor(vencedorForcado = participanteAtual)
                        return
                    }
                    participantes[participanteAtual].cor -> {
                        participantes[participanteAtual].ganharPontos(5)
                        println("${participantes[participanteAtual].nome} ganhou 5 pontos!")
                    }
                    else -> {
                        // Cor do adversário
                        participantes[participanteAtual].ganharPontos(1)
                        println("${participantes[participanteAtual].nome} ganhou 1 ponto!")
                    }
                }
            } else {
                // Par não encontrado
                println("Par não encontrado.")
                primeiraCarta?.virada = false
                segundaCarta?.virada = false

                // Verificar penalidade
                if (segundaCarta?.cor == CorCarta.PRETO || primeiraCarta?.cor == CorCarta.PRETO) {
                    println("${participantes[participanteAtual].nome} errou o par da carta preta e perdeu o jogo!")
                    anunciarVencedor(vencedorForcado = 1 - participanteAtual)
                    return
                }

                // Cor do adversário
                val corAdversario = participantes[1 - participanteAtual].cor
                if (primeiraCarta?.cor == corAdversario || segundaCarta?.cor == corAdversario) {
                    participantes[participanteAtual].perderPontos(2)
                    println("${participantes[participanteAtual].nome} perdeu 2 pontos!")
                }
            }

            // Pequena pausa para visualizar o resultado
            Thread.sleep(2000)
        }

        // Verifica se o jogo acabou (todos os pares foram encontrados)
        private fun verificarFimDeJogo(): Boolean {
            for (i in 0 until tamanhoTabuleiro) {
                for (j in 0 until tamanhoTabuleiro) {
                    if (tabuleiro[i][j]?.encontrada == false) {
                        return false
                    }
                }
            }
            return true
        }

        // Anuncia o vencedor do jogo
        private fun anunciarVencedor(vencedorForcado: Int? = null) {
            val vencedor = when {
                vencedorForcado != null -> vencedorForcado
                participantes[0].pontuacao > participantes[1].pontuacao -> 0
                participantes[1].pontuacao > participantes[0].pontuacao -> 1
                else -> -1 // Empate
            }

            println("\n=== FIM DE JOGO ===")
            println("Pontuações finais:")
            println("${participantes[0].nome}: ${participantes[0].pontuacao} pontos")
            println("${participantes[1].nome}: ${participantes[1].pontuacao} pontos")

            if (vencedor == -1) {
                println("O jogo terminou em EMPATE!")
            } else {
                println("${participantes[vencedor].nome} VENCEU o jogo!")
            }
        }

        // Exibe as pontuações atuais dos participantes
        private fun exibirPontuacoes() {
            if (participantes.isEmpty()) {
                println("\nAinda não há participantes registrados.")
                return
            }

            println("\n=== PONTUAÇÕES ===")
            participantes.forEach {
                println("${it.nome} (${it.cor}): ${it.pontuacao} pontos")
            }
        }

        // Exibe as regras do jogo
        private fun exibirRegras() {
            println("\n=== REGRAS DO JOGO ===")
            println("- O jogo da memória é jogado em um tabuleiro de cartas viradas para baixo.")
            println("- Cada jogador, na sua vez, vira duas cartas tentando encontrar pares.")
            println("- O tabuleiro 4x4 possui 8 pares de cartas com diferentes cores de fundo:")
            println("  * 2 pares com fundo AZUL")
            println("  * 2 pares com fundo VERMELHO")
            println("  * 3 pares com fundo AMARELO")
            println("  * 1 par com fundo PRETO")
            println("- Pontuação:")
            println("  * Par AMARELO: +1 ponto")
            println("  * Par da SUA COR: +5 pontos")
            println("  * Par da COR ADVERSÁRIA (acerto): +1 ponto")
            println("  * Par da COR ADVERSÁRIA (erro): -2 pontos")
            println("  * Par PRETO (acerto): Vitória automática")
            println("  * Par PRETO (erro): Derrota automática")
            println("- A pontuação nunca fica negativa (mínimo é zero).")
            println("- Após 3 tentativas inválidas, o jogador perde a vez.")
            println("- Vence quem tiver mais pontos no final (ou quem conseguir o par PRETO).")
        }
    }

    fun main() {
        val jogo = JogoDaMemoria()
        jogo.iniciar()
    }
}
