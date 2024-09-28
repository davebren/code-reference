package ui

import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CancellationException
import ui.PressExpandBackButtonState.*


@Composable
fun PressExpandBackAnimationScreen() {
  var containerSize by remember { mutableStateOf(IntSize.Zero) }

  Box(
    modifier = Modifier.fillMaxSize()
      .background(AppColors.homeBackground())
      .onSizeChanged { containerSize = it },
  ) {
    PressExpandBackButton(DpOffset(0.dp, 24.dp), containerSize)
    PressExpandBackButton(DpOffset(0.dp, 128.dp), containerSize)
  }
}

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun PressExpandBackButton(offset: DpOffset, containerSize: IntSize) {
  val buttonState = remember { MutableTransitionState(unpressed) }
  val transition = rememberTransition(buttonState)

  val transitionDuration = 200
  val pressedHeightChange = 8.dp
  val elevation by transition.animateDp(
    transitionSpec = { tween(durationMillis = transitionDuration) }
  ) { targetState ->
    when(targetState) {
      unpressed -> 2.dp
      pressed -> 8.dp
      expanded -> 8.dp
    }
  }

  val height by transition.animateDp(
    transitionSpec = { tween(durationMillis = transitionDuration) }
  ) { targetState ->
    when(targetState) {
      unpressed -> 60.dp
      pressed -> 60.dp + pressedHeightChange
      expanded -> with(LocalDensity.current) { containerSize.height.toDp() }
    }
  }

  val padding by transition.animateDp(
    transitionSpec = { tween(durationMillis = transitionDuration) }
  ) { targetState ->
    when(targetState) {
      unpressed -> 16.dp
      pressed -> 14.dp
      expanded -> 0.dp
    }
  }

  val yOffset by transition.animateDp(
    transitionSpec = { tween(durationMillis = transitionDuration) }
  ) { targetState ->
    when(targetState) {
      unpressed -> offset.y
      pressed -> offset.y - (pressedHeightChange * .5f)
      expanded -> 0.dp
    }
  }

  Card(
    backgroundColor = AppColors.button(),
    elevation = elevation,
    modifier = Modifier
      .fillMaxWidth()
      .offset(0.dp, yOffset)
      .zIndex(if (buttonState.targetState == expanded || buttonState.currentState == expanded) 1f else 0f)
      .padding(horizontal = padding)
      .height(height)
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            try {
              if (buttonState.currentState == unpressed) {
                buttonState.targetState = pressed
              }

              awaitRelease()
              if (buttonState.currentState == pressed || buttonState.targetState == pressed) {
                buttonState.targetState = expanded
              } else if (buttonState.currentState == expanded || buttonState.targetState == expanded) {
                buttonState.targetState = unpressed
              }
            } catch (cancel: CancellationException) {
              if (buttonState.currentState == pressed || buttonState.targetState == pressed) {
                buttonState.targetState = unpressed
              }
            }
          },
        )
      }
  ) {
    Text(
      textAlign = TextAlign.Center,
      text = buttonState.currentState.name,
      fontSize = 24.sp,
      fontWeight = FontWeight.SemiBold,
      color = AppColors.buttonText(),
      modifier = Modifier
        .wrapContentHeight()
    )
  }
}

enum class PressExpandBackButtonState {
  unpressed, pressed, expanded
}