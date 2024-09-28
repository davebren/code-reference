package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CancellationException


@Composable
fun PressReleaseCancelScreen() {
  var containerSize by remember { mutableStateOf(IntSize.Zero) }

  Box(
    modifier = Modifier.fillMaxSize()
      .background(AppColors.homeBackground())
      .onSizeChanged { containerSize = it },
  ) {
    PressReleaseCancelButton()
  }
}

@Composable
fun PressReleaseCancelButton() {
  Card(
    backgroundColor = AppColors.button(),
    modifier = Modifier
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            try {
              println("press")
              awaitRelease()
              println("release")
            } catch (cancel: CancellationException) {
              println("cancel")
            }
          },
        )
      }
      .fillMaxWidth()
      .padding(16.dp)
      .height(60.dp)
  ) {
    Text(
      textAlign = TextAlign.Center,
      text = "Press Test",
      fontSize = 24.sp,
      fontWeight = FontWeight.SemiBold,
      color = AppColors.buttonText(),
      modifier = Modifier
        .wrapContentHeight()
    )
  }
}