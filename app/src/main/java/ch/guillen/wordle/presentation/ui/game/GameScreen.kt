package ch.guillen.wordle.presentation.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.guillen.wordle.presentation.theme.HitColors
import ch.guillen.wordle.presentation.theme.WordleTheme
import ch.guillen.wordle.presentation.ui.dialog.ExitWarningDialog
import ch.guillen.wordle.presentation.ui.keyboard.KeyType
import ch.guillen.wordle.presentation.ui.keyboard.Keyboard
import ch.guillen.wordle.presentation.ui.keyboard.KeyboardState
import java.util.*

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    onFinishedGame: () -> Unit = {}
) {
    val words = viewModel.words.collectAsState().value
    val states = viewModel.wordState.collectAsState().value
    val keyboardState = viewModel.keyboardState.collectAsState().value

    Game(
        words = words,
        states = states,
        keyboardState = keyboardState,
        onKeyPress = { key ->
            viewModel.didType(key)
        },
    )

    ExitWarningDialog(onExitGame = onFinishedGame)
}

@Composable
private fun Game(
    words: List<Word>,
    states: List<WordState>,
    keyboardState: KeyboardState = KeyboardState(),
    onKeyPress: (KeyType) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(6) {
                val word = words.getOrNull(it)?.word ?: ""
                val state = states.getOrNull(it) ?: WordState.Idle
                WordView(
                    word = word,
                    state = state
                )
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
private fun WordView(
    word: String,
    state: WordState?
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { iteration ->
            when {
                state == WordState.Idle ->
                    Letter(
                        modifier = Modifier.weight(0.2f),
                        character = word.getOrElse(iteration) { ' ' },
                    )
                state is WordState.Validated ->
                    RevealingLetter(
                        modifier = Modifier.weight(0.2f),
                        character = word.getOrElse(iteration) { ' ' },
                        hitColor = state.status.getOrNull(iteration)?.color ?: HitColors.Unknown,
                        iteration
                    )
                state == WordState.Error -> {
                    WordError(
                        char = word.getOrElse(iteration) { ' ' },
                        modifier = Modifier
                            .weight(0.2f)
                    )
                }
                state is WordState.GameOver && state.isVictory ->
                    VictoryLetter(
                        modifier = Modifier.weight(0.2f),
                        character = word.getOrElse(iteration) { ' ' },
                        iteration
                    )
                else -> {  }
            }
        }
    }
}

@Composable
private fun RevealingLetter(
    modifier: Modifier,
    character: Char,
    hitColor: Color,
    index: Int
) {
    val animationProgress = remember { Animatable(1f) }
    val previousFrame = remember { mutableStateOf(2f) }
    LaunchedEffect(animationProgress) {
        animationProgress.animateTo(
            targetValue = 0f,
            animationSpec = repeatable(
                animation = tween(
                    durationMillis = 300,
                    delayMillis = 200 * index,
                ),
                repeatMode = RepeatMode.Reverse,
                iterations = 2
            )
        )
    }

    val scaleY = if(animationProgress.value == 0f) 1f else animationProgress.value

    Letter(
        modifier = modifier
            .scale(scaleX = 1f, scaleY = scaleY),
        character = character,
        hitColor = if(previousFrame.value <= scaleY) hitColor else HitColors.Unknown
    )

    previousFrame.value = scaleY
}

@Composable
private fun VictoryLetter(
    modifier: Modifier,
    character: Char,
    index: Int
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(animationProgress) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = repeatable(
                iterations = 2,
                animation = tween(
                    durationMillis = 300,
                    delayMillis = 80 * index
                ),
                repeatMode = RepeatMode.Reverse,
            )
        )
    }

    val scaleY = if(animationProgress.value == 1f) 0f else animationProgress.value

    val travelDistance = with(LocalDensity.current) { 24.dp.toPx() }
    Letter(
        modifier = modifier
            .graphicsLayer {
                translationY = -scaleY * travelDistance
            },
        character = character,
        hitColor = HitColors.Hit
    )
}

@Composable
private fun WordError(
    char: Char,
    modifier: Modifier
) {
    val animationProgress = remember { Animatable(1f) }
    LaunchedEffect(animationProgress) {
        animationProgress.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium,
            )
        )
    }
    Letter(
        modifier = modifier
            .offset(x = Dp(animationProgress.value * -10)),
        character = char,
    )
}

@Composable
private fun Letter(
    modifier: Modifier,
    character: Char,
    hitColor: Color = HitColors.Unknown
) {
    Text(
        modifier = modifier
            .background(hitColor)
            .aspectRatio(1f)
            .wrapContentSize(Alignment.Center)
            .padding(4.dp),
        text = character.titlecase(Locale.ROOT),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onBackground,
        fontSize = 40.sp
    )
}

@Preview(showBackground = true)
@Composable
private fun DarkGame() {
    val words = mutableListOf(Word("TASTE"))
    val states = mutableListOf(
        WordState.Validated(
            listOf(
                CharStatus.ABSENT,
                CharStatus.ABSENT,
                CharStatus.CORRECT,
                CharStatus.PRESENT,
                CharStatus.ABSENT
            )
        )
    )
    WordleTheme(darkTheme = true) {
        Game(words, states)
    }
}