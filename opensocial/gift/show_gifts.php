<?php

require_once 'db.php';

if( isset($_POST['t']) ) {
    $flag = $_POST['t'];
}
else {
    echo "{}";
}

if( isset($_POST['uid']) ) {
    $uid = $_POST['uid'];
}
else {
    echo "{}";
}

if( $flag == 'sent' ) {
    $sql = "select `from`, `to`, `gid`, ts from gifts where `from` = $uid limit 10";
}
else {
    $sql = "select `from`, `to`, `gid`, ts from gifts where `to` = $uid limit 10";
}

$gifts = $db->get_results($sql);

if ($gifts) {
    $records = array();
    foreach ($gifts as $r) {
       $record['from'] = $r->from;
       $record['to'] = $r->to;
       $record['gid'] = $r->gid;
       $records[] = $record;
    }
}
echo json_encode($records);

?>
