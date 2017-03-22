
echo "Input file interactions: $1"
echo "Input file users: $2"
echo "Input file contents: $3"
echo "Output file: $4"

echo "Generating cards.."

java -cp deck/target/carddeck-jar-with-dependencies.jar com.vionel.carddeck.deck.CommandLineInvoker -input_uci_interactions $1 -input_uci_users $2 -input_uci_contents $3 -output $4

echo "Cards generated"
