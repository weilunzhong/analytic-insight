cd deck

if mvn assembly:assembly -DdescriptorId=jar-with-dependencies
then
	echo
	echo "Deck built successfully"
	echo "________________________________________________"
	echo
else
	echo
	echo "Deck build failed"
	echo "________________________________________"
	echo
	exit 1
fi
