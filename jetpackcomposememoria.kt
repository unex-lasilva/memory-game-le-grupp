//Tiago fez o código e Diogo corrigiu os erros

package com.example.jogodamemoria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

enum class CorCarta {
    AZUL, VERMELHO, AMARELO, PRETO
}
//Tiago
data class Carta(val id: Int, val cor: CorCarta, var virada: Boolean = false, var encontrada: Boolean = false) {
    override fun toString(): String {
        return if (virada || encontrada) {
            when (cor) {
                CorCarta.AZUL -> "Azul$id"
                CorCarta.VERMELHO -> "Vermelho$id"
                CorCarta.AMARELO -> "Amarelo$id"
                CorCarta.PRETO -> "Preto$id"
            }
        } else {
            "???"
        }
    }
}

data class Participante(val nome: String, val cor: CorCarta, var pontuacao: Int = 0)

//Tiago
@Composable
fun JogoDaMemoriaUI() {
    var participantes by remember { mutableStateOf(arrayOf<Participante>()) }
    var tabuleiro by remember { mutableStateOf(Array(4) { Array(4) { null as Carta? } }) }
    var turnoAtual by remember { mutableStateOf(0) }
    var cartaViradaAtualmente by remember { mutableStateOf<Carta?>(null) }

    //Tiago
    // Função para inicializar o jogo
    fun inicializarJogo() {
        val cartas = mutableListOf<Carta>().apply {
            add(Carta(1, CorCarta.AZUL))
            add(Carta(1, CorCarta.AZUL))
            add(Carta(2, CorCarta.VERMELHO))
            add(Carta(2, CorCarta.VERMELHO))
            add(Carta(3, CorCarta.AMARELO))
            add(Carta(3, CorCarta.AMARELO))
            add(Carta(4, CorCarta.PRETO))
            add(Carta(4, CorCarta.PRETO))
            shuffle()
        }

        tabuleiro = Array(4) { Array(4) { cartas.removeAt(0) } }
        participantes = arrayOf(
            Participante("Jogador 1", CorCarta.AZUL),
            Participante("Jogador 2", CorCarta.VERMELHO)
        )
    }

    //Tiago
    // Função para verificar a correspondência das cartas
    fun verificarPar(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>) {
        val carta1 = tabuleiro[pos1.first][pos1.second]
        val carta2 = tabuleiro[pos2.first][pos2.second]
        if (carta1?.id == carta2?.id) {
            carta1?.encontrada = true
            carta2?.encontrada = true
        }
    }

    //Tiago
    // Exibição do tabuleiro
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jogo da Memória", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { inicializarJogo() }) {
            Text("Iniciar Jogo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Tiago
        // Exibição das cartas
        Column {
            tabuleiro.forEachIndexed { i, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEachIndexed { j, carta ->
                        CartaView(carta, onClick = {
                            if (!carta!!.virada) {
                                carta.virada = true
                                if (cartaViradaAtualmente == null) {
                                    cartaViradaAtualmente = carta
                                } else {
                                    verificarPar(Pair(i, j), Pair(i, j)) // Verifica as cartas
                                }
                            }
                        })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Turno: Jogador ${turnoAtual + 1}", style = MaterialTheme.typography.h6)
        Text("Pontuação: ${participantes[turnoAtual].pontuacao}", style = MaterialTheme.typography.body1)
    }
}

//Tiago
@Composable
fun CartaView(carta: Carta?, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .width(60.dp)
            .height(60.dp)
    ) {
        Text(text = carta?.toString() ?: "???")
    }
}

//Tiago
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JogoDaMemoriaUI()
}
