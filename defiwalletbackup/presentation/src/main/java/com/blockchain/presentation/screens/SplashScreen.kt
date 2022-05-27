package com.blockchain.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blockchain.componentlib.basic.Image
import com.blockchain.componentlib.basic.ImageResource
import com.blockchain.componentlib.button.ButtonState
import com.blockchain.componentlib.button.PrimaryButton
import com.blockchain.componentlib.control.PrimarySwitch
import com.blockchain.componentlib.navigation.NavigationBar
import com.blockchain.componentlib.theme.AppTheme
import com.blockchain.componentlib.theme.Grey900
import com.blockchain.presentation.BackPhraseDestination
import com.blockchain.presentation.R

@Composable
fun Splash(navController: NavController) {
    SplashScreen(backUpNowOnClick = {
        val route = BackPhraseDestination.DefaultPhrase.route
        navController.navigate(route)
    })
}

@Composable
fun SplashScreen(backUpNowOnClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationBar(
            title = stringResource(id = R.string.secure_defi_wallets),
            onBackButtonClick = { }
        )

        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.standard_margin)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.standard_margin),
                    end = dimensionResource(id = R.dimen.standard_margin),
                    bottom = dimensionResource(id = R.dimen.standard_margin)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BackupStatus()

            Spacer(modifier = Modifier.weight(1F))

            SplashScreenBackupDescription()

            Spacer(modifier = Modifier.weight(3F))

            SplashScreenCta(backUpNowOnClick = backUpNowOnClick)
        }
    }
}

@Composable
fun SplashScreenBackupDescription() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(imageResource = ImageResource.Local(R.drawable.ic_padlock))

        Spacer(Modifier.size(dimensionResource(R.dimen.standard_margin)))

        Text(
            text = stringResource(R.string.lets_backup_your_wallet),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.title2,
            color = Grey900
        )

        Spacer(Modifier.size(dimensionResource(R.dimen.tiny_margin)))

        Text(
            text = stringResource(R.string.back_up_splash_description),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.paragraph1,
            color = Grey900
        )
    }
}

@Composable
fun SplashScreenCta(backUpNowOnClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isChecked by remember { mutableStateOf(false) }

        Row {
            PrimarySwitch(
                isChecked = isChecked, onCheckChanged = { isChecked = it })

            Spacer(Modifier.size(dimensionResource(R.dimen.very_small_margin)))

            Text(
                text = stringResource(id = R.string.backup_phrase_checkbox_warning),
                style = AppTheme.typography.micro2
            )
        }

        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.standard_margin)))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.back_up_now),
            state = if (isChecked) ButtonState.Enabled else ButtonState.Disabled,
            onClick = backUpNowOnClick
        )
    }
}

// ///////////////
// PREVIEWS
// ///////////////

@Preview(name = "Splash", showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen {
    }
}

@Preview(name = "Splash Backup Description", showBackground = true)
@Composable
fun PreviewSplashScreenBackupDescription() {
    SplashScreenBackupDescription()
}
