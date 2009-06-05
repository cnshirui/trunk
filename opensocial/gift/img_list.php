<?php

require_once 'config.php';

$outa = array();
$pagesize = 24;

if( isset($_REQUEST['t']) && $_REQUEST['t'] == 'json' ) {
    $cnt = 0;
    $jjj = 0;

    $sql = "select id, imgurl from freehug_images order by id";

    $imgs = $db->get_results($sql);

    if ($imgs) {
        foreach ($imgs as $r) {
            $file = $r->imgurl;
            if ( $file != 'hug_smile_attach.gif' ) {
//                    if($jjj >= $start && $jjj < $stop ) {
                       //$temp = $cnt . ':"' . $file . '"';
                       $temp = $r->id . ':"' . $file . '"';
                       $outa[] = $temp;
                       $cnt++;
//                    }
                    $jjj++;
            }
        }
    }
}

$out = '{' . implode(",",$outa) . '}';
echo $out;

?>
