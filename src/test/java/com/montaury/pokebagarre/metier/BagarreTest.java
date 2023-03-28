package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

// SCENARIOS DE TEST
/*

demarrer :


validerNomPokemons :
-quand premier nom vide (erreur) V
-quand deuxieme nom vide (erreur) V
-quand 2 noms identiques (erreur) V

determinerVainqueur :
-quand premier doit gagner V
-quand deuxieme doit gagner V

Contact api :
-ne fonctionne pas


 */

class BagarreTest {

    @Test
    void should_throw_error_when_first_empty(){
        //GIVEN
        var testBagarre = new Bagarre();

        //WHEN
        var throwable = catchThrowable(() -> testBagarre.demarrer("", "Pikachu"));
        //THEN
        assertThat(throwable)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void should_throw_error_when_second_empty(){
        //GIVEN
        var testBagarre = new Bagarre();

        //WHEN
        var throwable = catchThrowable(() -> testBagarre.demarrer("Pikachu",""));

        //THEN
        assertThat(throwable)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void should_throw_error_when_both_same(){
        //GIVEN
        var testBagarre = new Bagarre();

        //WHEN
        var throwable = catchThrowable(() -> testBagarre.demarrer("Pikachu", "Pikachu"));

        //THEN
        assertThat(throwable)
                .isInstanceOf(ErreurMemePokemon.class)
                .hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void should_the_first_win(){
        //GIVEN
        var fausseApi = Mockito.mock(PokeBuildApi.class);
        var testBagarre = new Bagarre(fausseApi);

        Mockito.when(fausseApi.recupererParNom("Gagnant"))
                .thenReturn(CompletableFuture.completedFuture(new Pokemon("Gagnant",
                        "url1", new Stats(100, 100))));
        Mockito.when(fausseApi.recupererParNom("Perdant"))
                .thenReturn(CompletableFuture.completedFuture(new Pokemon("Perdant",
                        "url1", new Stats(1, 1))));
        //WHEN
        var futurVainqueur = testBagarre.demarrer("Gagnant", "Perdant");
        //THEN
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {
                    assertThat(pokemon.getNom()).isEqualTo("Gagnant");
                    });
    }

    @Test
    void should_the_second_win(){
        //GIVEN
        var fausseApi = Mockito.mock(PokeBuildApi.class);
        var testBagarre = new Bagarre(fausseApi);

        Mockito.when(fausseApi.recupererParNom("Gagnant"))
                .thenReturn(CompletableFuture.completedFuture(new Pokemon("Gagnant",
                        "url1", new Stats(100, 100))));
        Mockito.when(fausseApi.recupererParNom("Perdant"))
                .thenReturn(CompletableFuture.completedFuture(new Pokemon("Perdant",
                        "url1", new Stats(1, 1))));
        //WHEN
        var futurVainqueur = testBagarre.demarrer("Perdant", "Gagnant");
        //THEN
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {
                    assertThat(pokemon.getNom()).isEqualTo("Gagnant");
                });
    }

    @Test
    void should_throw_error_if_pokemon_doesnt_exist(){
        //GIVEN
        var fausseApi = Mockito.mock(PokeBuildApi.class);
        var testBagarre = new Bagarre(fausseApi);

        Mockito.when(fausseApi.recupererParNom("nomAuPif"))
                .thenThrow(new ErreurRecuperationPokemon("nomAuPif"));
        //WHEN
        var throwable = catchThrowable(() ->  testBagarre.demarrer("nomAuPif", "unAutreNom"));
        //THEN
        assertThat(throwable)
                .isInstanceOf(ErreurRecuperationPokemon.class)
                .hasMessage("Impossible de recuperer les details sur 'nomAuPif'");
    }

    @Test
    void api_contact_failed(){
        //GIVEN
        var fausseApi = Mockito.mock(PokeBuildApi.class);
        var testBagarre = new Bagarre(fausseApi);

        Mockito.when(fausseApi.recupererParNom("PremierPokemon")).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("PremierPokemon")));
        Mockito.when(fausseApi.recupererParNom("SecondPokemon")).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("SecondPokemon")));
        //WHEN
        var futurVainqueur = testBagarre.demarrer("PremierPokemon", "SecondPokemon");
        //THEN

        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
                .withThrowableOfType(ExecutionException.class)
                .havingCause()
                .isInstanceOf(ErreurRecuperationPokemon.class);
    }
}