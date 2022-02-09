package ch.guillen.wordle.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.*
import ch.guillen.wordle.presentation.theme.HitColors
import ch.guillen.wordle.presentation.theme.WordleTheme
import ch.guillen.wordle.presentation.ui.keyboard.KeyType
import ch.guillen.wordle.presentation.ui.keyboard.Keyboard
import ch.guillen.wordle.presentation.ui.keyboard.KeyboardState
import java.util.*

class SceneActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WordleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}

@Composable
fun GameScreen(
    viewModel: SceneViewModel = viewModel()
) {
    val state = viewModel.gameState
    Game(
        state = state,
        onKeyPress = { key ->
            viewModel.didType(key)
        },
    )
}

@Composable
private fun Game(
    state: SceneState,
    onKeyPress: (KeyType) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Word(
                word = state.word1.word,
                state = state.word1.state,
                solution = state.solution
            )
        }

        val missed = state.getMissedLetters()
        val keyboardState = KeyboardState().apply {
            missed.forEach {
                updateLetter(it, HitColors.Miss)
            }
        }
        Keyboard(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = keyboardState,
            onKeyPress = onKeyPress
        )
    }
}

@Composable
private fun Word(
    word: String,
    state: WordState = WordState.IDLE,
    solution: String
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { iteration ->
            when(state) {
                WordState.IDLE ->
                    Letter(
                        modifier = Modifier.weight(0.2f),
                        character = word.getOrElse(iteration) { ' ' },
                        hitType = HitType.UNKNOWN
                    )
                WordState.DONE ->
                    Letter(
                        modifier = Modifier.weight(0.2f),
                        character = word.getOrElse(iteration) { ' ' },
                        hitType = getHitTypeFromSolution(solution, word, iteration)
                    )
                WordState.ERROR -> {
                    WordError(
                        char = word.getOrElse(iteration) { ' ' },
                        modifier = Modifier
                            .weight(0.2f)
                    )
                }

            }
        }
    }
}

@Composable
private fun WordError(
    char: Char,
    modifier: Modifier
) {
    var targetValue by remember { mutableStateOf((-10).dp) }
    val animationProgress by animateDpAsState(
        targetValue = targetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    )
    SideEffect { targetValue = 0.dp }
    Letter(
        modifier = modifier
            .offset(x = animationProgress),
        character = char,
        hitType = HitType.UNKNOWN
    )
}

fun getHitTypeFromSolution(solution: String, word: String, index: Int): HitType {
    val letterTyped = word.getOrNull(index) ?:
        // Place is empty
        return HitType.UNKNOWN

    if(solution[index] == letterTyped) {
        // Letter is correct
        return HitType.FULL
    }

    if(!solution.contains(letterTyped)) {
        // Letter is not even in solution
        return HitType.MISS
    }

    // We know that the letter is in the solution

//    val repetitionsInSolution = solution.count { it == letterTyped }
//    val repetitionsInWord = word.count { it == letterTyped }

    if(solution.contains(letterTyped)/* && repetitionsInSolution == repetitionsInWord*/) {
        return HitType.PARTIAL
    }

    return HitType.MISS
}

@Composable
private fun Letter(
    modifier: Modifier,
    character: Char,
    hitType: HitType
) {
    val color by animateColorAsState(getColorByHit(hitType))

    Text(
        modifier = modifier
            .background(color)
            .aspectRatio(1f)
            .wrapContentSize(Alignment.Center)
            .padding(4.dp),
        text = character.titlecase(Locale.ROOT),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onBackground,
        fontSize = 40.sp
    )
}

fun getColorByHit(hitType: HitType) : Color = when(hitType) {
    HitType.UNKNOWN -> HitColors.Unknown
    HitType.MISS -> HitColors.Miss
    HitType.PARTIAL -> HitColors.Partial
    HitType.FULL -> HitColors.Hit
}

enum class HitType {
    UNKNOWN, MISS, PARTIAL, FULL
}

@Preview(showBackground = true)
@Composable
private fun LightGame() {
    val state = SceneState(
        solution = "TESTY",
        word1 = Word("TASTY", WordState.DONE)
    )
    WordleTheme(darkTheme = false) {
        Game(state)
    }
}

@Preview(showBackground = true)
@Composable
private fun DarkGame() {
    val state = SceneState(
        solution = "TESTY",
        word1 = Word("TASTE", WordState.DONE)
    )
    WordleTheme(darkTheme = true) {
        Game(state)
    }
}