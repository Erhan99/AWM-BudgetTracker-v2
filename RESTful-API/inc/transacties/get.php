<?php

$sql = "SELECT 
            tr_id, tr_bedrag, tr_mededeling, tr_begunstigde, tr_img, 
            tr_dt_id, tr_ct_id, tr_bl_id, dt_datum, dt_jaar, dt_maand, 
            dt_maand_num, dt_dag, ct_naam 
        FROM 
            Transacties 
        JOIN 
            Datums ON tr_dt_id = dt_id 
        JOIN 
            Categorieën ON tr_ct_id = ct_id";

$result = $conn->query($sql);

if (!$result) {
    $response['code'] = 7;
    $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
    $response['data'] = $conn->error;
    deliver_response($response);
    exit;
}


$data = [];
while ($row = $result->fetch_assoc()) {
    if (isset($row['tr_img']) && !empty($row['tr_img'])) {
        $row['tr_img'] = base64_encode($row['tr_img']);
    } else {
        $row['tr_img'] = null;
    }
    $data[] = $row;
}

$result->free();
$conn->close();

$response['data'] = json_encode($data, JSON_UNESCAPED_UNICODE);
deliver_JSONresponse($response);

exit;
?>
