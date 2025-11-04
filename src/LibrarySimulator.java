/*
 * Github Repository:
 * https://github.com/iD7m1/Library-Simulation-Project
 * 
 * Abdulrahman Alamer
 * 445102743
 */

import java.util.Scanner;

public class LibrarySimulator {

    // Predefined accounts
    static Member member1 = new Member(445102743, "Abdulrahman", 0);
    static Member member2 = new Member(445102744, "Abdulaziz", 0);
    static Member member3 = new Member(445102745, "Abdullah", 0);

    // Global statistics (persist across sessions)
    static double totalRevenue = 0.0; // accumulated fees from all borrow operations
    static int totalBorrowOperations = 0; // count of borrow operations across all users
    static int totalReturnOperations = 0; // count of return operations across all users

    // Constants
    static final double FEE_PER_BORROW = 0.50;
    static final int MAX_BORROW_PER_USER = 5;

    // Pause duration (ms) used after welcome messages and invalid inputs
    static final int PAUSE_MS = 1000;

    // ANSI Color Codes
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
    static final String BOLD = "\u001B[1m";

    // Box drawing characters
    static final String TOP_LEFT = "╔";
    static final String TOP_RIGHT = "╗";
    static final String BOTTOM_LEFT = "╚";
    static final String BOTTOM_RIGHT = "╝";
    static final String HORIZONTAL = "═";
    static final String VERTICAL = "║";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        clearScreen();
        displayWelcomeBanner();
        pause(PAUSE_MS);

        while (true) {
            clearScreen();
            drawBox("Main Menu", new String[] {
                    "1) Select a user account",
                    "2) Login as Administrator",
                    "3) Exit program"
            }, CYAN);
            System.out.print(BOLD + YELLOW + "Enter choice (1-3): " + RESET);

            String choice = input.nextLine().trim();
            if (choice.equals("1")) {
                handleAccountSelection(input);
            } else if (choice.equals("2")) {
                handleAdministratorMenu(input);
            } else if (choice.equals("3")) {
                clearScreen();
                displayGoodbyeMessage();
                break;
            } else {
                displayError("Invalid choice. Please enter 1, 2 or 3.");
                pause(PAUSE_MS);
            }
        }

        input.close();
    }

    // Show account list and let user pick one
    static void handleAccountSelection(Scanner input) {
        while (true) {
            clearScreen();
            drawBox("Account Selection Menu", new String[] {
                    String.format("1) %d - %s", member1.getId(), member1.getName()),
                    String.format("2) %d - %s", member2.getId(), member2.getName()),
                    String.format("3) %d - %s", member3.getId(), member3.getName()),
                    "4) Back to main menu"
            }, BLUE);
            System.out.print(BOLD + YELLOW + "Enter choice (1-4): " + RESET);

            String sel = input.nextLine().trim();
            if (sel.equals("1")) {
                userSession(input, 1);
            } else if (sel.equals("2")) {
                userSession(input, 2);
            } else if (sel.equals("3")) {
                userSession(input, 3);
            } else if (sel.equals("4")) {
                displayInfo("Returning to main menu.");
                pause(PAUSE_MS);
                return;
            } else {
                displayError("Invalid selection. Choose 1-4.");
                pause(PAUSE_MS);
            }
        }
    }

    // User session for a particular account (accountIndex 1..3)
    static void userSession(Scanner input, int accountIndex) {
        // Reset session statistics
        int sessionBorrowedCount = 0;
        int sessionReturnedCount = 0;
        double sessionFees = 0.0;

        String userName;
        int currentBorrowed;

        // Reference to the selected account's borrowed count via local alias
        if (accountIndex == 1) {
            userName = member1.getName();
            currentBorrowed = member1.getBorrowedCount();
        } else if (accountIndex == 2) {
            userName = member2.getName();
            currentBorrowed = member2.getBorrowedCount();
        } else {
            userName = member3.getName();
            currentBorrowed = member3.getBorrowedCount();
        }

        clearScreen();
        displayUserWelcome(userName,
                (accountIndex == 1 ? member1.getId() : (accountIndex == 2 ? member2.getId() : member3.getId())));
        pause(PAUSE_MS);

        // Session loop
        while (true) {
            clearScreen();
            drawBox("User Operations Menu", new String[] {
                    "1) View Borrowed Books Count",
                    "2) Borrow Book",
                    "3) Return Book",
                    "4) View Session Summary",
                    "5) Exit to Account Selection Menu"
            }, GREEN);
            System.out.print(BOLD + YELLOW + "Enter choice (1-5): " + RESET);

            String action = input.nextLine().trim();
            if (action.equals("1")) {
                displayInfo(String.format("You currently have %d book(s) borrowed.", currentBorrowed));
                pause(PAUSE_MS);
            } else if (action.equals("2")) {
                // Borrow one book
                if (currentBorrowed >= MAX_BORROW_PER_USER) {
                    displayError("Cannot borrow more than " + MAX_BORROW_PER_USER + " books at once.");
                    pause(PAUSE_MS);
                } else {
                    currentBorrowed++;
                    sessionBorrowedCount++;
                    sessionFees += FEE_PER_BORROW;
                    totalRevenue += FEE_PER_BORROW;
                    totalBorrowOperations++;
                    displaySuccess(
                            String.format("Book borrowed successfully. Fee charged: $%.2f. You now have %d book(s).",
                                    FEE_PER_BORROW, currentBorrowed));
                    pause(PAUSE_MS);
                }
            } else if (action.equals("3")) {
                // Return one book
                if (currentBorrowed <= 0) {
                    displayError("You have no books to return.");
                    pause(PAUSE_MS);
                } else {
                    currentBorrowed--;
                    sessionReturnedCount++;
                    totalReturnOperations++;
                    displaySuccess(
                            String.format("Book returned successfully. You now have %d book(s).", currentBorrowed));
                    pause(PAUSE_MS);
                }
            } else if (action.equals("4")) {
                // Session summary
                clearScreen();
                displaySessionSummary(sessionBorrowedCount, sessionReturnedCount, sessionFees);
                System.out.print(BOLD + YELLOW + "\nPress Enter to continue..." + RESET);
                input.nextLine();
            } else if (action.equals("5")) {
                // TODO

                // // Save the updated currentBorrowed back to the selected account
                // if (accountIndex == 1) {
                // booksBorrowed1 = currentBorrowed;
                // } else if (accountIndex == 2) {
                // booksBorrowed2 = currentBorrowed;
                // } else {
                // booksBorrowed3 = currentBorrowed;
                // }

                displayInfo("Session ended. Returning to account selection menu.");
                pause(PAUSE_MS);
                break;
            } else {
                displayError("Invalid option. Please enter a number between 1 and 5.");
                pause(PAUSE_MS);
            }
        }
    }

    // Administrator menu
    static void handleAdministratorMenu(Scanner input) {
        clearScreen();
        displayAdminLogin();
        pause(PAUSE_MS);

        while (true) {
            clearScreen();
            drawBox("Administrator Menu", new String[] {
                    "1) View Total Revenue",
                    "2) Most Frequent Operation (borrow/return)",
                    "3) Exit to Main Menu"
            }, RED);
            System.out.print(BOLD + YELLOW + "Enter choice (1-3): " + RESET);

            String adm = input.nextLine().trim();
            if (adm.equals("1")) {
                displayInfo(String.format("Total revenue collected: $%.2f", totalRevenue));
                pause(PAUSE_MS);
            } else if (adm.equals("2")) {
                if (totalBorrowOperations == 0 && totalReturnOperations == 0) {
                    displayInfo("No operations have been performed yet.");
                    pause(PAUSE_MS);
                } else if (totalBorrowOperations > totalReturnOperations) {
                    displayInfo(String.format("Most frequent operation: borrow (%d times)", totalBorrowOperations));
                    pause(PAUSE_MS);
                } else if (totalReturnOperations > totalBorrowOperations) {
                    displayInfo(String.format("Most frequent operation: return (%d times)", totalReturnOperations));
                    pause(PAUSE_MS);
                } else {
                    displayInfo(String.format("Both operations are equally frequent: borrow (%d) and return (%d)",
                            totalBorrowOperations, totalReturnOperations));
                    pause(PAUSE_MS);
                }
            } else if (adm.equals("3")) {
                displayInfo("Exiting administrator menu.");
                pause(PAUSE_MS);
                break;
            } else {
                displayError("Invalid choice. Please enter 1, 2 or 3.");
                pause(PAUSE_MS);
            }
        }
    }

    // Pause helper: sleeps for ms milliseconds, handles interruption properly
    static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Clear screen helper: tries platform clear commands, then ANSI, then newlines
    static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process p;
            if (os.contains("win")) {
                // use cmd /c cls on Windows
                p = new ProcessBuilder("cmd", "/c", "cls").inheritIO().start();
                p.waitFor();
            } else {
                // try unix clear command
                p = new ProcessBuilder("clear").inheritIO().start();
                p.waitFor();
            }
        } catch (Exception e) {
            // fallback to ANSI escape sequence
            try {
                final String ANSI_CLS = "\u001b[2J\u001b[H";
                System.out.print(ANSI_CLS);
                System.out.flush();
            } catch (Exception ex) {
                // last resort: print several newlines
                for (int i = 0; i < 50; i++)
                    System.out.println();
            }
        }
    }

    // UI Helper Methods

    static void drawBox(String title, String[] options, String color) {
        int maxWidth = title.length();
        for (String option : options) {
            if (option.length() > maxWidth)
                maxWidth = option.length();
        }
        maxWidth += 4;

        System.out.println();
        System.out.println(color + BOLD + TOP_LEFT + repeat(HORIZONTAL, maxWidth) + TOP_RIGHT + RESET);
        System.out.println(color + BOLD + VERTICAL + center(title, maxWidth) + VERTICAL + RESET);
        System.out.println(color + BOLD + TOP_LEFT + repeat(HORIZONTAL, maxWidth) + TOP_RIGHT + RESET);

        for (String option : options) {
            System.out.println(color + VERTICAL + RESET + " " + WHITE + option
                    + repeat(" ", maxWidth - option.length() - 1) + color + VERTICAL + RESET);
        }

        System.out.println(color + BOLD + BOTTOM_LEFT + repeat(HORIZONTAL, maxWidth) + BOTTOM_RIGHT + RESET);
        System.out.println();
    }

    static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
            sb.append(str);
        return sb.toString();
    }

    static String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        return repeat(" ", padding) + text + repeat(" ", width - text.length() - padding);
    }

    static void displayWelcomeBanner() {
        System.out.println();
        System.out.println(CYAN + BOLD + "╔════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "║                                                ║" + RESET);
        System.out.println(
                CYAN + BOLD + "║   " + GREEN + "Welcome to the Library Simulator" + CYAN + "             ║" + RESET);
        System.out.println(CYAN + BOLD + "║                                                ║" + RESET);
        System.out.println(CYAN + BOLD + "╚════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void displayGoodbyeMessage() {
        System.out.println();
        System.out.println(CYAN + BOLD + "╔════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "║                                                ║" + RESET);
        System.out.println(
                CYAN + BOLD + "║   " + YELLOW + "Thank you for using the Library!" + CYAN + "             ║" + RESET);
        System.out.println(
                CYAN + BOLD + "║   " + WHITE + "Goodbye!" + CYAN + "                                     ║" + RESET);
        System.out.println(CYAN + BOLD + "║                                                ║" + RESET);
        System.out.println(CYAN + BOLD + "╚════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void displayUserWelcome(String userName, int accountId) {
        System.out.println();
        System.out.println(GREEN + BOLD + "╔════════════════════════════════════════════════╗" + RESET);
        System.out.println(
                GREEN + BOLD + "║   " + WHITE + "User Login Successful" + GREEN + "                        ║" + RESET);
        System.out.println(GREEN + BOLD + "║                                                ║" + RESET);
        System.out.println(GREEN + BOLD + "║   " + CYAN + String.format("Hello, %s!", userName)
                + repeat(" ", 37 - userName.length()) + GREEN + "║" + RESET);
        System.out.println(GREEN + BOLD + "║   " + WHITE + String.format("Account ID: %d", accountId)
                + repeat(" ", 31 - String.valueOf(accountId).length()) + GREEN + "  ║" + RESET);
        System.out.println(GREEN + BOLD + "╚════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void displayAdminLogin() {
        System.out.println();
        System.out.println(RED + BOLD + "╔════════════════════════════════════════════════╗" + RESET);
        System.out.println(RED + BOLD + "║                                                ║" + RESET);
        System.out.println(
                RED + BOLD + "║   " + YELLOW + "*** Administrator Login ***" + RED + "                  ║" + RESET);
        System.out.println(RED + BOLD + "║                                                ║" + RESET);
        System.out.println(RED + BOLD + "╚════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void displaySessionSummary(int borrowed, int returned, double fees) {
        System.out.println();
        System.out.println(CYAN + BOLD + "╔════════════════════════════════════════════════╗" + RESET);
        System.out.println(
                CYAN + BOLD + "║   " + WHITE + "Session Summary" + CYAN + "                              ║" + RESET);
        System.out.println(CYAN + BOLD + "╠════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + BOLD + "║   " + GREEN + String.format("Books borrowed this session: %d", borrowed)
                + repeat(" ", 16 - String.valueOf(borrowed).length()) + CYAN + "║" + RESET);
        System.out.println(CYAN + BOLD + "║   " + YELLOW + String.format("Books returned this session: %d", returned)
                + repeat(" ", 16 - String.valueOf(returned).length()) + CYAN + "║" + RESET);
        System.out.println(CYAN + BOLD + "║   " + WHITE + String.format("Total fees this session: $%.2f", fees)
                + repeat(" ", 19 - String.format("%.2f", fees).length()) + CYAN + "║" + RESET);
        System.out.println(CYAN + BOLD + "╚════════════════════════════════════════════════╝" + RESET);
    }

    static void displaySuccess(String message) {
        System.out.println("\n" + GREEN + BOLD + "✓ " + message + RESET);
    }

    static void displayError(String message) {
        System.out.println("\n" + RED + BOLD + "✗ Error: " + message + RESET);
    }

    static void displayInfo(String message) {
        System.out.println("\n" + CYAN + BOLD + "ℹ " + message + RESET);
    }

}
