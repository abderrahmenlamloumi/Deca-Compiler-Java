package fr.ensimag.deca;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.ensimag.deca.codegen.Utf8Scalar;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args) throws InterruptedException {
        // example log4j message.
        LOG.info("Decac compiler started");
        AtomicBoolean error = new AtomicBoolean(false);
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            CompilerOptions.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner() && args.length == 1) {
            DecacMain.printBanner();
            return;
        }
        if (options.getSourceFiles().isEmpty()) {
            CompilerOptions.displayUsage();
            return;
        }
        if (options.getParallel()) {
            // On crée un ThreadPool avec le nombre de fichiers à compiler comme taille
            List<File> filesToCompile = options.getSourceFiles();
            ExecutorService executor = Executors.newFixedThreadPool(Math.min(filesToCompile.size(),
                    Runtime.getRuntime().availableProcessors()));
            List<Callable<Void>> tasks = new ArrayList<>();
            // On crée une tâche par fichier à compiler, pour l'exécuter dans son propre thread
            for (File source : filesToCompile) {
                tasks.add(() -> {
                    // Log du nom du thread courant et du fichier qu'on compile dans ce thread
                    LOG.info("Compilation lancée par le thread : " + Thread.currentThread().getName() +
                            " sur fichier " + source.getName());

                    DecacCompiler compiler = new DecacCompiler(options, source);
                    if (compiler.compile()) {
                        error.set(true);
                    }
                    return null;
                });
            }

            // exécuter toutes les tâches et après on arrête l'executor
            executor.invokeAll(tasks);
            executor.shutdown();
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error.set(true);
                }
            }
        }
        LOG.debug("Nombre de Threads utilisés : " + Thread.activeCount());
        System.exit(error.get() ? 1 : 0);
    }

    public static void printBanner() {

        System.out.println("+--------------------------------------------------------------------------------------------------+");
        System.out.println("|                                                                                                  |");
        System.out.println("|                                                                                                  |");
        System.out.println("|     /\\\\\\\\\\\\\\\\\\\\\\\\           /\\\\\\                           /\\\\\\              /\\\\\\\\\\\\\\            |");
        System.out.println("|    /\\\\\\//////////           \\/\\\\\\                       /\\\\\\\\\\\\\\            /\\\\\\/////\\\\\\         |");
        System.out.println("|    /\\\\\\                      \\/\\\\\\                      \\/////\\\\\\           /\\\\\\    \\//\\\\\\       |");
        System.out.println("|    \\/\\\\\\    /\\\\\\\\\\\\\\          \\/\\\\\\                          \\/\\\\\\          \\/\\\\\\     \\/\\\\\\      |");
        System.out.println("|     \\/\\\\\\   \\/////\\\\\\          \\/\\\\\\                          \\/\\\\\\          \\/\\\\\\     \\/\\\\\\     |");
        System.out.println("|      \\/\\\\\\       \\/\\\\\\          \\/\\\\\\                          \\/\\\\\\          \\/\\\\\\     \\/\\\\\\    |");
        System.out.println("|       \\/\\\\\\       \\/\\\\\\          \\/\\\\\\                          \\/\\\\\\          \\//\\\\\\    /\\\\\\    |");
        System.out.println("|        \\//\\\\\\\\\\\\\\\\\\\\\\\\/           \\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\              \\/\\\\\\           \\///\\\\\\\\\\\\\\/    |");
        System.out.println("|          \\////////////             \\///////////////               \\///              \\///////     |");
        System.out.println("|                                                                                                  |");
        System.out.println("|                                                                                                  |");
        System.out.println("+--------------------------------------------------------------------------------------------------+");
        System.out.println("\n\n");
        System.out.println("Clément FRÉVILLE    |    Abderrahmen LAMLOUMI     |     Tonin CELDRAN       |    Richard GALDEANO ");
        System.out.println();
        System.out.println("Nathan GUILLERMO    |       Ahmed KARMOUS         |    Gabriel VALENTE      |    Ahmed Oussama BEN DAOU");
        System.out.println("\n\n");

    }

}
