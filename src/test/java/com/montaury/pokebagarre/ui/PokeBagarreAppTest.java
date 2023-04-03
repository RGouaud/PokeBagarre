package com.montaury.pokebagarre.ui;

import java.util.concurrent.TimeUnit;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static com.montaury.pokebagarre.fixtures.ConstructeurDePokemon.unPokemon;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class PokeBagarreAppTest {
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1 = "#nomPokemon1";
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2 = "#nomPokemon2";
    private static final String IDENTIFIANT_BOUTON_BAGARRE = ".button";
    @Start
    private void start(Stage stage) {
        new PokeBagarreApp().start(stage);
    }
    @Test
    void nom_du_test(FxRobot robot) {
        //robot.clickOn(IDENTIFIANT);
        //robot.write("Text");
        //await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
        //assertThat(...).isEqualTo(...)
        // );
    }
    private static String getResultatBagarre(FxRobot robot) {
        return robot.lookup("#resultatBagarre").queryText().getText();
    }
    private static String getMessageErreur(FxRobot robot) {
        return robot.lookup("#resultatErreur").queryLabeled().getText();
    }

    @Test
    void should_the_first_win(FxRobot robot){
        var pokemon1 = "Pikachu";
        var pokemon2 = "Magicarpe";
        //actions robot
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write(pokemon1);
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write(pokemon2);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(getResultatBagarre(robot)).isEqualTo("Le vainqueur est: "+pokemon1)
        );
    }

    @Test
    void should_the_second_win(FxRobot robot){
        var pokemon1 = "Magicarpe";
        var pokemon2 = "Pikachu";
        //actions robot
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write(pokemon1);
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write(pokemon2);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getResultatBagarre(robot)).isEqualTo("Le vainqueur est: "+pokemon2)
        );
    }

    @Test
    void should_throw_an_error_when_both_same(FxRobot robot){
        var pokemon = "Tortank";
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write(pokemon);
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write(pokemon);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(3, TimeUnit.SECONDS).untilAsserted(()->
            assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de faire se bagarrer un pokemon avec lui-meme")
        );
    }

    @Test
    void should_throw_an_error_when_first_is_empty(FxRobot robot){
        var pokemon2 = "Tortank";
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write(pokemon2);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(()->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Le premier pokemon n'est pas renseigne")
        );
    }

    @Test
    void should_throw_an_error_when_second_is_empty(FxRobot robot){
        var pokemon1 = "Tortank";
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write(pokemon1);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(()->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Le second pokemon n'est pas renseigne")
        );
    }

    @Test
    void should_throw_an_error_when_first_doesn_t_exist(FxRobot robot){
        var pokemon1 = "Michmich le pokemon";
        var pokemon2 = "Tortank";
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write(pokemon1);
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write(pokemon2);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(()->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de recuperer les details sur '" + pokemon1 + "'")
        );
    }

    @Test
    void should_throw_an_error_when_second_doesn_t_exist(FxRobot robot){
        var pokemon1 = "Tortank";
        var pokemon2 = "Michmich le pokemon";
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write(pokemon1);
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write(pokemon2);
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(()->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de recuperer les details sur '" + pokemon2 + "'")
        );
    }
}