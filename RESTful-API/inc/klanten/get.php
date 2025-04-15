<?php

$sql = "SELECT 
			kl_id, kl_naam, kl_voornaam, kl_email, 
			kl_wachtwoord, kl_isAdmin, 
			bl_id, bl_inkomsten, bl_uitgaven 
		FROM 
			Klanten 
		JOIN 
			Balansen 
		ON 
			kl_id = bl_kl_id ";

$result = $conn -> query($sql);

if (!$result) {
	$response['code'] = 7;
	$response['status'] = $api_response_code[$response['code']]['HTTP Response'];
	$response['data'] = $conn->error;
	deliver_response($response);
}

$response['data'] = getJsonObjFromResult($result); 

$result->free();

$conn->close();
deliver_JSONresponse($response);

exit;
?>